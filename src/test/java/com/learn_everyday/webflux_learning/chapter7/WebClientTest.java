package com.learn_everyday.webflux_learning.chapter7;

import com.learn_everyday.webflux_learning.chapter7.dto.Calculator;
import com.learn_everyday.webflux_learning.chapter7.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public class WebClientTest {

    private static final Logger log = LoggerFactory.getLogger(WebClientTest.class);

    AbstractWebClient abstractWebClient = new AbstractWebClient();

    private final WebClient webClient = abstractWebClient.createWebClient();
    private final WebClient webClientWithHeader = abstractWebClient.createWebClient(header -> header.defaultHeader("java", "secret"));
    private final WebClient webClientWithAuth = abstractWebClient.createWebClient(header -> header.defaultHeaders(auth -> auth.setBasicAuth("java", "secret")));
    private final WebClient webClientWithBearerAuth = abstractWebClient.createWebClient(header -> header.defaultHeaders(auth -> auth.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")));
    private final WebClient webClientWithGenBearerAuth = abstractWebClient.createWebClient(
            builder -> builder.filter(generateToken()).filter(requestLogger()));

    @Test
    public void getTest() throws InterruptedException {
        this.webClient.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    public void concurrentGetTest() throws InterruptedException {
        for (int i = 1; i <= 50; i++) {
            this.webClient.get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(abstractWebClient.print())
                    .subscribe();
        }
        Thread.sleep(Duration.ofSeconds(5));
    }

    @Test
    public void getStreamingResponseTest() throws InterruptedException {
        this.webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void postBodyValueTest() {
        var mono = new Product(null, "iwatch", 500);
        this.webClient.post()
                .uri("/lec03/product")
                .bodyValue(mono)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    // Send the request payload asynchronously.
    @Test
    public void postBodyTest() {
        var mono = Mono.fromSupplier(() -> new Product(null, "iwatch", 500))
                .delayElement(Duration.ofSeconds(1));
        this.webClient.post()
                .uri("/lec03/product")
                .body(mono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void defaultHeaderTest() {
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void overrideDefaultHeaderTest() {
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "new-service")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void multipleHeadersTest() {
        var map = Map.of("caller-id", "order-service",
                "new-key", "new-value");
        this.webClientWithHeader.get()
                .uri("/lec04/product/{id}", 1)
                .headers(httpHeaders -> httpHeaders.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void errorHandlerTest() {
        this.webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 20, 10)
                .header("operation", "@")
                .retrieve()
                .bodyToMono(Calculator.class)
                .doOnError(WebClientResponseException.class, ex -> log.info("{}", ex.getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new Calculator(0, 0, null, 0.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new Calculator(0, 0, null, -1.0))
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void retrieveResponseDetailsTest() {

        this.webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 20, 10)
                .header("operation", "+")
                .exchangeToMono(this::decodeResponse)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<Calculator> decodeResponse(ClientResponse response) {
//        response.cookies();
//        response.headers();
        log.info("Status Code: {}", response.statusCode());
        if (response.statusCode().isError()) {
            return response.bodyToMono(ProblemDetail.class)
                    .doOnNext(problemDetail -> log.info("{}", problemDetail))
                    .then(Mono.empty());
        }
        return response.bodyToMono(Calculator.class);
    }


    @Test
    public void queryParamTest() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(10, 20, "+"))
                .retrieve()
                .bodyToMono(Calculator.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void queryParamUsingMapTest() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        var map = Map.of("first", 10,
                "second", 20,
                "operation", "+");
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(Calculator.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void basicAuthTest() {
        this.webClientWithAuth.get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void bearerAuthTest() {
        this.webClientWithBearerAuth.get()
                .uri("/lec08/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(WebClientResponseException.class, ex -> log.info("{}", ex.getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.Unauthorized.class, new Product(0, null, 0))
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void exchangeFilterTest() {
        this.webClientWithGenBearerAuth.get()
                .uri("/lec09/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(abstractWebClient.print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private ExchangeFilterFunction generateToken() {
        return (request, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("Generated Token: {}", token);
            var modifiedRequest = ClientRequest.from(request).headers(httpHeaders -> httpHeaders.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        };
    }

    private ExchangeFilterFunction requestLogger() {
        return (request, next) -> {
            var isEnabled = (Boolean) request.attributes().getOrDefault("enable-logging", false);
            if (isEnabled) {
                log.info("URL: {}", request.url());
                log.info("HTTP Method: {}", request.method());
            }
            return next.exchange(request);
        };
    }

    @Test
    public void webClientAttributeTest() {
        for (int i = 1; i <= 4; i++) {
            this.webClientWithGenBearerAuth.get()
                    .uri("/lec09/product/{id}", i)
                    .attribute("enable-logging", i % 2 == 0)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(abstractWebClient.print())
                    .then()
                    .as(StepVerifier::create)
                    .expectComplete()
                    .verify();
        }
    }

}

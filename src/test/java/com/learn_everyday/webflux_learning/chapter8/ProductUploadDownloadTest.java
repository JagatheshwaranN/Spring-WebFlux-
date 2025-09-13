package com.learn_everyday.webflux_learning.chapter8;

import com.learn_everyday.webflux_learning.chapter8.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.time.Duration;

public class ProductUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadDownloadTest.class);
    private final ProductClient client = new ProductClient();

    @Test
    public void uploadTest() {
//        var flux = Flux.just(new ProductDto(null, "Smart Phone", 10000))
//                .delayElements(Duration.ofSeconds(10));

//        var flux = Flux.range(1, 5)
//                .map(counter -> new ProductDto(null, "Product" + counter, counter * 10))
//                .delayElements(Duration.ofSeconds(2));

        var flux = Flux.range(1, 10000)
                .map(counter -> new ProductDto(null, "Product" + counter, counter * 10));

        this.client.uploadProductsTest(flux)
                .doOnNext(uploadResponse -> log.info("Received: {}", uploadResponse))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void download() {
        this.client.downloadProductsTest()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("./src/test/java/com/learn_everyday/webflux_learning/chapter8/products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}

package com.learn_everyday.webflux_learning.chapter8;

import com.learn_everyday.webflux_learning.chapter8.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter8.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

    private final WebClient client = WebClient
            .builder().baseUrl("http://localhost:8080")
            .build();

    public Mono<UploadResponse> uploadProductsTest(Flux<ProductDto> productDtoFlux) {
        return this.client
                .post()
                .uri("products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(productDtoFlux, ProductDto.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    public Flux<ProductDto> downloadProductsTest() {
        return this.client
                .get()
                .uri("products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

}

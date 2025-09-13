package com.learn_everyday.webflux_learning.chapter8.controller;

import com.learn_everyday.webflux_learning.chapter8.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter8.dto.UploadResponse;
import com.learn_everyday.webflux_learning.chapter8.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProduct(@RequestBody Flux<ProductDto> productDtoFlux) {
        log.info("Upload Product Invoked");
        return this.productService.saveProduct(productDtoFlux
                        //.doOnNext(productDto -> log.info("Received: {}", productDto))
                )
                .then(this.productService.getProductCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProduct() {
        return this.productService.getAllProducts();
    }

}

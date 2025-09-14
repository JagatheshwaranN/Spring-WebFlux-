package com.learn_everyday.webflux_learning.chapter9.controller;

import com.learn_everyday.webflux_learning.chapter9.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter9.dto.UploadResponse;
import com.learn_everyday.webflux_learning.chapter9.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;


    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return this.productService.saveProduct(productDtoMono);
    }

    @GetMapping(value = "stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> productStream(@PathVariable Integer maxPrice) {
        return this.productService.productStream()
                .filter(dto -> dto.price() <= maxPrice);
    }


}

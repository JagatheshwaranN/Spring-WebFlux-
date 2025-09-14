package com.learn_everyday.webflux_learning.chapter9.service;

import com.learn_everyday.webflux_learning.chapter9.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter9.mapper.EntityDtoMapper;
import com.learn_everyday.webflux_learning.chapter9.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Sinks.Many<ProductDto> sink;


    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(EntityDtoMapper::toEntity)
                .flatMap(this.productRepository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(this.sink::tryEmitNext);
    }

    public Flux<ProductDto> productStream() {
        return this.sink.asFlux();
    }

}

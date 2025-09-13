package com.learn_everyday.webflux_learning.chapter8.service;

import com.learn_everyday.webflux_learning.chapter8.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter8.mapper.EntityDtoMapper;
import com.learn_everyday.webflux_learning.chapter8.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> saveProduct(Flux<ProductDto> productDtoFlux) {
        return productDtoFlux.map(EntityDtoMapper::toEntity)
                .as(this.productRepository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductCount() {
        return this.productRepository.count();
    }

}

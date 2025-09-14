package com.learn_everyday.webflux_learning.chapter9.service;

import com.learn_everyday.webflux_learning.chapter9.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSetupService implements CommandLineRunner {


    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1, 100)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> new ProductDto(null, "Product"+i, ThreadLocalRandom.current().nextInt(1, 100)))
                .flatMap(dto -> this.productService.saveProduct(Mono.just(dto)))
                .subscribe();
    }

}

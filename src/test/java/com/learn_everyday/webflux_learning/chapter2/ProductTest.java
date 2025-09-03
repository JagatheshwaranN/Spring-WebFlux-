package com.learn_everyday.webflux_learning.chapter2;

import com.learn_everyday.webflux_learning.chapter2.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

public class ProductTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(ProductTest.class);

    @Autowired
    ProductRepository productRepository;

    @Test
    public void findProductByPrice() {
        this.productRepository.findByPriceBetween(200, 1000)
                .doOnNext(product -> log.info("{}", product))
                .as(StepVerifier::create)
                .expectNextCount(7L)
                .expectComplete()
                .verify();
    }

    @Test
    public void paginatedProducts() {
        this.productRepository.findBy(PageRequest.of(1, 2).withSort(Sort.by("price").ascending()))
                .doOnNext(product -> log.info("{}", product))
                .as(StepVerifier::create)
                .assertNext(product -> Assertions.assertEquals(300, product.getPrice()))
                .assertNext(product -> Assertions.assertEquals(400, product.getPrice()))
                .expectComplete()
                .verify();
    }

}

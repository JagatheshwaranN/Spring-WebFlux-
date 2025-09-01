package com.learn_everyday.webflux_learning.chapter1.error_handle;

import com.learn_everyday.webflux_learning.chapter1.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("traditional/error")
public class TraditionalWebErrorController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalWebErrorController.class);

    private final RestClient restClient = RestClient
            .builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl("http://localhost:7070")
            .build();

    @GetMapping("products/notorious")
    public List<Product> getProducts() {
        var productList = this.restClient
                .get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("Response Received : {}", productList);
        return productList;
    }

}

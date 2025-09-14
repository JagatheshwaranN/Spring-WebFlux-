package com.learn_everyday.webflux_learning.chapter9.config;

import com.learn_everyday.webflux_learning.chapter9.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class AppConfig {

    @Bean
    public Sinks.Many<ProductDto> sinks() {
        return Sinks.many().replay().limit(1);
    }

}

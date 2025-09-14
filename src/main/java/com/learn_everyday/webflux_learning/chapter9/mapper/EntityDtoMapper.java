package com.learn_everyday.webflux_learning.chapter9.mapper;

import com.learn_everyday.webflux_learning.chapter9.dto.ProductDto;
import com.learn_everyday.webflux_learning.chapter9.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto productDto) {
        var product = new Product();
        product.setId(productDto.id());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        return product;
    }

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getDescription(), product.getPrice());
    }

}

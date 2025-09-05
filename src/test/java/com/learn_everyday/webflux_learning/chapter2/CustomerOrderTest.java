package com.learn_everyday.webflux_learning.chapter2;

import com.learn_everyday.webflux_learning.chapter2.dto.OrderDetails;
import com.learn_everyday.webflux_learning.chapter2.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class CustomerOrderTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerOrderTest.class);

    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    public void productsOrderByCustomer() {
        this.customerOrderRepository.getProductsOrderByCustomer("mike")
                .doOnNext(product -> log.info("{}", product))
                .as(StepVerifier::create)
                .expectNextCount(2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void orderDetails() {
        this.customerOrderRepository.getOrderDetails("iphone 20")
                .doOnNext(orderDetails -> log.info("{}", orderDetails))
                .as(StepVerifier::create)
                .assertNext(order -> Assertions.assertEquals(975, order.amount()))
                .assertNext(order -> Assertions.assertEquals(950, order.amount()))
                .expectComplete()
                .verify();
    }

    @Test
    public void orderDetailsUsingDatabaseClient() {
        this.databaseClient.sql("""
                                SELECT
                                    co.order_id,
                                    c.name AS customer_name,
                                    p.description AS product_name,
                                    co.amount,
                                    co.order_date
                                FROM
                                    customer c
                                INNER JOIN customer_order co ON c.id = co.customer_id
                                INNER JOIN product p ON p.id = co.product_id
                                WHERE
                                    p.description = :description
                                ORDER BY co.amount DESC
                        """)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(orderDetails -> log.info("{}", orderDetails))
                .as(StepVerifier::create)
                .assertNext(order -> Assertions.assertEquals(975, order.amount()))
                .assertNext(order -> Assertions.assertEquals(950, order.amount()))
                .expectComplete()
                .verify();

    }

}

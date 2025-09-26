package com.learneveryday.aggregator.validator;

import com.learneveryday.aggregator.dto.TradeRequest;
import com.learneveryday.aggregator.exceptions.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<TradeRequest>> validate() {
        return tradeRequestMono -> tradeRequestMono
                .filter(hasTicker())
                .switchIfEmpty(ApplicationException.missingTicker())
                .filter(hasTradeAction())
                .switchIfEmpty(ApplicationException.missingTradeAction())
                .filter(isValidQuantity())
                .switchIfEmpty(ApplicationException.invalidQuantity());
    }


    private static Predicate<TradeRequest> hasTicker() {
        return tradeRequest -> Objects.nonNull(tradeRequest.ticker());
    }

    private static Predicate<TradeRequest> hasTradeAction() {
        return tradeRequest -> Objects.nonNull(tradeRequest.tradeAction());
    }

    private static Predicate<TradeRequest> isValidQuantity() {
        return tradeRequest -> Objects.nonNull(tradeRequest.quantity()) && tradeRequest.quantity() > 0;
    }

}

package com.learn_everyday.webflux_learning.chapter8.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId, Long productCount) {
}

package com.ms_products.dto;

import lombok.Builder;

@Builder
public record RegisterOrderResponseDTO(Integer httpResponseStatus, String message) {
}
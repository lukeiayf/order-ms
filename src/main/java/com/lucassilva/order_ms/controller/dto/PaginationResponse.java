package com.lucassilva.order_ms.controller.dto;

import org.springframework.data.domain.Page;

public record PaginationResponse(Integer page, Integer size, Long totalItems, Integer totalPages) {

    public static PaginationResponse fromPage(Page<?> page){
        return new PaginationResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}

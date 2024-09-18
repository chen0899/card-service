package com.demo.card_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponseDto {

    private String bin;

    private String alphaCode;

    private String bank;
}


package com.demo.card_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CardRequestDto {

    @NotNull(message = "Card number cannot be null")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String card;
}

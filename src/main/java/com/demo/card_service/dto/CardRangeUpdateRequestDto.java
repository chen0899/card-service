package com.demo.card_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CardRangeUpdateRequestDto {

    private String bin;

    @JsonProperty("min_range")
    private String minRange;

    @JsonProperty("max_range")
    private String maxRange;

    @JsonProperty("alpha_code")
    private String alphaCode;

    @JsonProperty("bank_name")
    private String bankName;
}

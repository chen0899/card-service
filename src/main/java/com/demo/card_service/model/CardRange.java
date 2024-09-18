package com.demo.card_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_ranges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CardRange {

    @Id
    private String bin;

    @Column(name = "min_range")
    private String minRange;

    @Column(name = "max_range")
    private String maxRange;

    @Column(name = "alpha_code")
    private String alphaCode;

    @Column(name = "bank_name")
    private String bankName;
}

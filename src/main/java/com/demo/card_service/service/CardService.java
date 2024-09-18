package com.demo.card_service.service;

import com.demo.card_service.dto.CardRequestDto;
import com.demo.card_service.dto.CardResponseDto;
import com.demo.card_service.model.CardRange;
import org.springframework.data.domain.Page;

public interface CardService {

    CardResponseDto getCardRange(CardRequestDto cardRequestDto);

    Page<CardRange> getAll(int page, int size);
}

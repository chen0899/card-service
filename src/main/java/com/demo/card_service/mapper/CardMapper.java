package com.demo.card_service.mapper;

import com.demo.card_service.dto.CardRangeUpdateRequestDto;
import com.demo.card_service.dto.CardResponseDto;
import com.demo.card_service.model.CardRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "bankName", target = "bank")
    CardResponseDto toResponseDto(CardRange cardRanges);

    CardRange toEntity(CardRangeUpdateRequestDto cardRangeUpdateRequestDto);

    List<CardResponseDto> toResponseDtoList(List<CardRange> cardRanges);
}

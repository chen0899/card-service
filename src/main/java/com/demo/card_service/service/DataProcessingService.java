package com.demo.card_service.service;

import com.demo.card_service.dto.CardRangeUpdateRequestDto;

import java.io.IOException;
import java.util.List;

public interface DataProcessingService {

    List<CardRangeUpdateRequestDto> fetchData() throws IOException;
}

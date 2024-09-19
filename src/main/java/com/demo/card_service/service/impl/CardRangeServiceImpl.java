package com.demo.card_service.service.impl;

import com.demo.card_service.dto.CardRangeUpdateRequestDto;
import com.demo.card_service.dto.CardRequestDto;
import com.demo.card_service.dto.CardResponseDto;
import com.demo.card_service.exception.CardUpdateException;
import com.demo.card_service.exception.NotFoundException;
import com.demo.card_service.mapper.CardMapper;
import com.demo.card_service.model.CardRange;
import com.demo.card_service.repository.CardRangeRepository;
import com.demo.card_service.service.CardService;
import com.demo.card_service.service.DataProcessingService;
import com.demo.card_service.utils.ScheduledTaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CardRangeServiceImpl implements CardService {

    public static final int CHUNK_SIZE = 100;

    private final CardRangeRepository repository;

    private final DataProcessingService dataProcessingService;

    private final CardMapper cardMapper;

    private final ScheduledTaskUtil scheduledTaskUtil;

    public CardRangeServiceImpl(DataProcessingService dataProcessingService, CardRangeRepository repository, CardMapper cardMapper, ScheduledTaskUtil scheduledTaskUtil) {
        this.dataProcessingService = dataProcessingService;
        this.repository = repository;
        this.cardMapper = cardMapper;
        this.scheduledTaskUtil = scheduledTaskUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponseDto getCardRange(CardRequestDto cardRequestDto) {
        String paddedCard = String.format("%-19s", cardRequestDto.getCard())
                .replace(' ', '0');

        return repository.findByCard(paddedCard)
                .map(cardMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Card range not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardRange> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateCardBins() {
        if(scheduledTaskUtil.isSchedulingEnabled()) {
            try {
                List<CardRangeUpdateRequestDto> newCards = dataProcessingService.fetchData();
                List<CardRange> existed = repository.findAll();

                Map<String, CardRange> existingCardBinMap = existed.stream()
                        .collect(Collectors.toMap(CardRange::getBin, cardBin -> cardBin));

                List<CardRange> toCreate = new ArrayList<>();
                List<CardRange> toUpdate = new ArrayList<>();

                for (CardRangeUpdateRequestDto newCardRange : newCards) {
                    CardRange existedCardRange = existingCardBinMap.remove(newCardRange.getBin());
                    CardRange newCardRangeEntity = cardMapper.toEntity(newCardRange);
                    if (existedCardRange != null) {
                        if (!existedCardRange.equals(newCardRangeEntity)) {
                            toUpdate.add(cardMapper.toEntity(newCardRange));
                        }
                    } else {
                        toCreate.add(cardMapper.toEntity(newCardRange));
                    }
                }

                List<CardRange> toDelete = new ArrayList<>(existingCardBinMap.values());


                int saved = saveInChunks(toCreate, CHUNK_SIZE);
                int updated = saveInChunks(toUpdate, CHUNK_SIZE);
                repository.deleteAll(toDelete);
                log.info("Created: {} \nUpdated: {}\nDeleted: {}", saved, updated, toDelete.size());
            } catch (Exception e) {
                throw new CardUpdateException(String.format("Error during card updates %s", e.getMessage()));
            }
        } else {
            log.info("Scheduling is disabled");
        }
    }

    private int saveInChunks(List<CardRange> records, int chunkSize) {
        int totalRecords = records.size();
        int totalSaved = 0;
        for (int start = 0; start < totalRecords; start += chunkSize) {
            int end = Math.min(totalRecords, start + chunkSize);
            List<CardRange> chunk = records.subList(start, end);
            totalSaved += repository.saveAll(chunk).size();
        }
        return totalSaved;
    }
}

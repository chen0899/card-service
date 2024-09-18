package com.demo.card_service.controller;

import com.demo.card_service.dto.CardRequestDto;
import com.demo.card_service.dto.CardResponseDto;
import com.demo.card_service.model.CardRange;
import com.demo.card_service.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/lookup")
    public ResponseEntity<CardResponseDto> lookupCard(@RequestBody @Valid CardRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardService.getCardRange(cardRequestDto));
    }

    @GetMapping()
    public ResponseEntity<Page<CardRange>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cardService.getAll(page, size));
    }
}
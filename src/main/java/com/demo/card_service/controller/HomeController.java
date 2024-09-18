package com.demo.card_service.controller;

import com.demo.card_service.dto.CardRequestDto;
import com.demo.card_service.dto.CardResponseDto;
import com.demo.card_service.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final CardService cardService;

    public HomeController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cards", Collections.emptyList());
        return "index";
    }

    @PostMapping("/search")
    public String searchCard(@RequestParam("cardNumber") String cardNumber, Model model) {
        List<CardResponseDto> cards = new ArrayList<>();
        cardNumber = cardNumber.replace(" ", "");
        if (cardNumber == null || cardNumber.length() != 16) {
            model.addAttribute("errorMessage", "Please enter a valid 16-digit card number.");
        } else {
            CardRequestDto cardRequestDto = new CardRequestDto(cardNumber);
            try {
                CardResponseDto cardResponseDto = cardService.getCardRange(cardRequestDto);
                cards.add(cardResponseDto);
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        model.addAttribute("cards", cards);
        return "index";
    }
}

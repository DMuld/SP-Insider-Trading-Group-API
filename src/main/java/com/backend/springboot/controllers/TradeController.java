package com.backend.springboot.controllers;

import com.backend.springboot.handlers.TradeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.backend.springboot.database.TradeRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @GetMapping("/getTrades")
    public String getTrades() {
        TradeHandler response = new TradeHandler();
        return String.format(response.getTrades(tradeRepository));
    }

    //Possible add setTrades

    //Possible add getSpecificTrade
}

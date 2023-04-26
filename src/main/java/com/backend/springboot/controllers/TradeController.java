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

    @GetMapping("/getTradeByKeyword")
    public String getTradeByKeyword(@RequestParam(value = "key")String key) {
    	TradeHandler response = new TradeHandler();
    	return String.format(response.getTradeByKeyword(tradeRepository, key));
    }
    
    @GetMapping("/getTradesByFavorites")
    public String getTradesByFavorites(@RequestParam(value = "favorites")String favorites) {
    	TradeHandler response = new TradeHandler();
    	return String.format(response.getTradeByFavorites(tradeRepository, favorites));
    }
}

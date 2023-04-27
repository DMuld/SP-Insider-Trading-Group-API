package com.backend.springboot.handlers;

import java.util.ArrayList;

import com.backend.springboot.database.Trade;
import com.backend.springboot.database.TradeRepository;

public class TradeHandler {
    public String getTrades(TradeRepository tradeRepository) {
        Iterable<Trade> allTrades = tradeRepository.findAll();
        String ret = new String("");
        for (Trade trade : allTrades){
            ret += trade.getTICKER() + "!" +
                    trade.getPUBLISHED() + "!" +
                    trade.getTRADED() + "!" +
                    trade.getFILEDAFTER().toString() + "!" +
                    trade.getTYPE() + "!" +
                    trade.getVOLUME().toString() + "!" +
                    trade.getPRICE().toString() + "!" +
                    "|";
        }
        return ret;
    }
    
    public String getTradeByKeyword(TradeRepository tradeRepository, String key) {
    	String allTrades = this.getTrades(tradeRepository);
    	String ret = "";
    	
    	String[] tradesArray = allTrades.split("\\|");
    	
    	for (String trade : tradesArray) {
    		if (trade.contains(key))
    		{
    			ret += trade + "|";
    		}
    	}
    	
    	return ret;
    }
    
    public String getTradeByFavorites(TradeRepository tradeRepository, String favorites) {
    	String[] favoritesArray = favorites.split(",");
    	Iterable<Trade> allTrades = tradeRepository.findAll();
    	ArrayList<Trade> allTradesList = new ArrayList<>();
    	ArrayList<Trade> favoriteTrades = new ArrayList<>();
    	
    	String ret = "";
    	
    	for (Trade trade : allTrades) {
    		allTradesList.add(trade);
    	}
    	
    	for (String favorite : favoritesArray) {
    		if (favorite.isEmpty()) continue;
    		
    		for (int i = 0; i < allTradesList.size(); i++) {
    			Trade trade = allTradesList.get(i);
    			
    			if (trade.getTICKER().equals(favorite)) {
    				favoriteTrades.add(trade);
    				allTradesList.remove(i);
    			}
    		}
    	}
    	
    	for (Trade trade : favoriteTrades){
            ret += trade.getTICKER() + "!" +
                    trade.getPUBLISHED() + "!" +
                    trade.getTRADED() + "!" +
                    trade.getFILEDAFTER().toString() + "!" +
                    trade.getTYPE() + "!" +
                    trade.getVOLUME().toString() + "!" +
                    trade.getPRICE().toString() + "!" +
                    "|";
        }
    	
    	return ret;
    }
}

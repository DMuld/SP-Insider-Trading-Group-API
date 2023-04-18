package com.backend.springboot.handlers;

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
}

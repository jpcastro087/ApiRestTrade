package com.api.trade.service;

import com.api.trade.dto.TradeDetailsDTO;

import java.util.List;

public interface TradeDetailService {
    List<TradeDetailsDTO> getTradeDetails();
}

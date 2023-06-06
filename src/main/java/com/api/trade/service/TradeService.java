package com.api.trade.service;

import com.api.trade.domain.Trade;
import com.api.trade.dto.TradeDTO;
import com.api.trade.dto.TradeDetailsDTO;
import com.api.trade.request.TradePisoRequest;
import com.api.trade.request.VentaRequest;

import java.util.List;

public interface TradeService {
    TradeDTO getTrade(String pair);
    List<TradeDTO> getActiveTrades();
    void vender(VentaRequest ventaRequest);
    void updatePiso(TradePisoRequest tradePisoRequest);
    void createPiso(TradePisoRequest tradePisoRequest);
    void deletePiso(TradePisoRequest tradePisoRequest);
    List<Trade> getPisoAndMoneda(Long piso, String moneda);
}

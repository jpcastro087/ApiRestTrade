package com.api.trade.service;

import com.api.trade.request.TradePisoRequest;

public interface PisoService {

    void updatePiso(TradePisoRequest tradePisoRequest);
    void createPiso(TradePisoRequest tradePisoRequest);
    void deletePiso(TradePisoRequest tradePisoRequest);
}

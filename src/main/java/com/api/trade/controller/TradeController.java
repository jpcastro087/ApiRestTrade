package com.api.trade.controller;

import com.api.trade.dto.TradeDTO;
import com.api.trade.dto.TradeDetailsDTO;
import com.api.trade.request.PisosGenerericosRequest;
import com.api.trade.request.TradePisoRequest;
import com.api.trade.request.VentaRequest;
import com.api.trade.service.PisoService;
import com.api.trade.service.TradeDetailService;
import com.api.trade.service.TradeService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller("/trade")
@ExecuteOn(TaskExecutors.IO)
@Slf4j
public class TradeController {

    @Inject
    private TradeService tradeService;
    @Inject
    private PisoService pisoService;
    @Inject
    private TradeDetailService tradeDetailService;


    @Get(uri = "/profit/{pair}", produces = MediaType.APPLICATION_JSON)
    public TradeDTO getProfit(@NotBlank String pair) {
        return tradeService.getTrade(pair);
    }

    @Get(uri = "/active/trades", produces = MediaType.APPLICATION_JSON)
    public List<TradeDTO> getActiveTrades() {
        return tradeService.getActiveTrades();
    }


    @Get(uri = "/details", produces = MediaType.APPLICATION_JSON)
    public List<TradeDetailsDTO> getDetails() {
        return tradeDetailService.getTradeDetails();
    }

    @Post(uri = "/venta", produces = MediaType.APPLICATION_JSON)
    public void vender(VentaRequest ventaRequest) {
        tradeService.vender(ventaRequest);
    }

    @Put(uri = "/update/piso", produces = MediaType.APPLICATION_JSON)
    public void updatePiso(TradePisoRequest tradePisoRequest) {
        pisoService.updatePiso(tradePisoRequest);
    }

    @Post(uri = "/create/piso", produces = MediaType.APPLICATION_JSON)
    public void createPiso(TradePisoRequest tradePisoRequest) {
        pisoService.createPiso(tradePisoRequest);
    }

    @Post(uri = "/delete/piso", produces = MediaType.APPLICATION_JSON)
    public void delete(TradePisoRequest tradePisoRequest) {
        pisoService.deletePiso(tradePisoRequest);
    }


    @Post(uri = "/create/pisosgenericos", produces = MediaType.APPLICATION_JSON)
    public void createPisosGenericos(PisosGenerericosRequest pisosGenerericosRequest) {
        tradeService.createTradesGenericos(pisosGenerericosRequest);
    }

}

package com.api.trade.controller;

import com.api.trade.domain.MonedaVolumen;
import com.api.trade.dto.TradeDetailsDTO;
import com.api.trade.service.MonedaVolumenService;
import com.api.trade.service.TradeDetailService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller("/monedavolumen")
@ExecuteOn(TaskExecutors.IO)
@Slf4j
public class MonedaVolumenController {

    @Inject
    private MonedaVolumenService monedaVolumenService;

    @Get(uri = "/all", produces = MediaType.APPLICATION_JSON)
    public List<MonedaVolumen> getMonedasVolumen() {
        return monedaVolumenService.getAll();
    }
}

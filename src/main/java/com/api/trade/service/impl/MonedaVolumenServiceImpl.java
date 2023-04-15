package com.api.trade.service.impl;

import com.api.trade.domain.MonedaVolumen;
import com.api.trade.repository.MonedaVolumenRepository;
import com.api.trade.repository.PisoRepository;
import com.api.trade.service.MonedaVolumenService;
import jakarta.inject.Inject;

import java.util.List;

public class MonedaVolumenServiceImpl implements MonedaVolumenService {


    @Inject
    private MonedaVolumenRepository monedaVolumenRepository;

    @Override
    public List<MonedaVolumen> getAll() {
        return (List<MonedaVolumen>) monedaVolumenRepository.findAll();
    }
}

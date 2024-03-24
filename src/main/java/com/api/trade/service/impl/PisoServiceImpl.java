package com.api.trade.service.impl;

import com.api.trade.domain.CurrencyToTrack;
import com.api.trade.domain.Piso;
import com.api.trade.domain.Trade;
import com.api.trade.repository.CurrencyToTrackRepository;
import com.api.trade.repository.PisoRepository;
import com.api.trade.repository.TradeRepository;
import com.api.trade.request.TradePisoRequest;
import com.api.trade.service.PisoService;
import jakarta.inject.Inject;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class PisoServiceImpl implements PisoService {

    @Inject
    private PisoRepository pisoRepository;
    @Inject
    private CurrencyToTrackRepository currencyToTrackRepository;

    private TradeRepository tradeRepository;

    @Override
    public void updatePiso(TradePisoRequest tradePisoRequest) {
        Piso piso = pisoRepository.findById(tradePisoRequest.getIdPiso()).get();
        piso.setTakeprofit(String.valueOf(tradePisoRequest.getTakeProfit() / 100));
        piso.setPorcentajebajada(String.valueOf(tradePisoRequest.getPorcentajeBajada() / 100));
        piso.setPorcentajedinero(String.valueOf(tradePisoRequest.getPorcentajeInvertido() / 100));
        if (piso.getNro().equals(1l)) {
            piso.setMargen(String.valueOf(tradePisoRequest.getMargen() / 100));
        }

        pisoRepository.update(piso);
    }

    @Override
    public void createPiso(TradePisoRequest tradePisoRequest) {
        Piso piso = new Piso();
        piso.setNro(tradePisoRequest.getNro());
        piso.setPair(tradePisoRequest.getMoneda());
        piso.setTakeprofit(String.valueOf(tradePisoRequest.getTakeProfit() / 100));
        piso.setPorcentajebajada(String.valueOf(tradePisoRequest.getPorcentajeBajada() / 100));
        piso.setPorcentajedinero(String.valueOf(tradePisoRequest.getPorcentajeInvertido() / 100));
        if (piso.getNro().equals(1l)) {
            piso.setMargen(String.valueOf(tradePisoRequest.getMargen() / 100));
        } else {
            piso.setMargen(String.valueOf(0d));
        }

        String moneda = getStringRemoveFiat(tradePisoRequest.getMoneda());
        Optional<CurrencyToTrack> monedaObtenida = currencyToTrackRepository.getByMoneda(moneda);

        if (!monedaObtenida.isPresent()) {
            CurrencyToTrack currencyToTrack = new CurrencyToTrack();
            currencyToTrack.setMoneda(moneda);
            currencyToTrackRepository.save(currencyToTrack);
        }

        pisoRepository.save(piso);
    }

    @Override
    public void deletePiso(TradePisoRequest tradePisoRequest) {
        Long nroPiso = tradePisoRequest.getNro();
        String moneda = tradePisoRequest.getMoneda();

        Optional<BigInteger> optionalPisoMaximo = pisoRepository.getPisoMaximoByMoneda(moneda);

        if(!optionalPisoMaximo.isPresent()) {return;}
        Long a = optionalPisoMaximo.get().longValue();
        if(!a.equals(nroPiso)) return;

        //si el valor nroPiso no es igual al maximo no elimina nada.


        Optional<List<Trade>> trades = tradeRepository.getByPisoAndMoneda(nroPiso, moneda);

        if(trades.isEmpty()){
            Piso piso = new Piso();
            piso.setId(tradePisoRequest.getIdPiso());
            pisoRepository.delete(piso);
        }

        Optional<List<Piso>> pisos = pisoRepository.getByPair(moneda);

        if(pisos.isEmpty()){
            String currency = getStringRemoveFiat(moneda);
            Optional<CurrencyToTrack> optionalCurrencyToTrack = currencyToTrackRepository.getByMoneda(currency);
            CurrencyToTrack currencyToTrack = optionalCurrencyToTrack.get();
            currencyToTrackRepository.delete(currencyToTrack);
        }
    }

    private String getStringRemoveFiat(String moneda) {
        String resultado = moneda.substring(0, moneda.length() - 4);
        return resultado;
    }
}

package com.api.trade.service.impl;

import com.api.trade.domain.CurrencyToTrack;
import com.api.trade.domain.Piso;
import com.api.trade.domain.Trade;
import com.api.trade.dto.TradeDTO;
import com.api.trade.repository.CurrencyToTrackRepository;
import com.api.trade.repository.PisoRepository;
import com.api.trade.repository.TradeRepository;
import com.api.trade.request.TradePisoRequest;
import com.api.trade.request.VentaRequest;
import com.api.trade.service.TradeService;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TradeServiceImpl implements TradeService {
    @Inject
    private TradeRepository tradeRepository;
    @Inject
    private PisoRepository pisoRepository;
    @Inject
    private CurrencyToTrackRepository currencyToTrackRepository;

    @Override
    public TradeDTO getTrade(String pair) {
        Trade trade = tradeRepository.getOpenTrade(pair);
        TradeDTO tradeDTO = getDTO(trade);

        return tradeDTO;
    }

    @Override
    public List<TradeDTO> getActiveTrades() {
        List<Trade> activeTrades = tradeRepository.getActiveTrades();
        List<TradeDTO> activeTradesDTO = new ArrayList<TradeDTO>();
        for (Trade trade : activeTrades) {


        }
        return activeTradesDTO;
    }

    @Override
    public void vender(VentaRequest ventaRequest) {
        Piso piso = pisoRepository.findById(ventaRequest.getIdPiso()).get();
        piso.setTakeprofit(String.valueOf((ventaRequest.getPorcentaje() - 0.05) / 100));
        pisoRepository.update(piso);
    }

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

    @Override
    public List<Trade> getPisoAndMoneda(Long piso, String moneda) {
        return null;
    }


    private TradeDTO getDTO(Trade trade) {
        Double currentPrice = Double.valueOf(trade.getCurrentprice());
        Double entryPrice = Double.valueOf(trade.getEntryprice());
        Double profit = (currentPrice - entryPrice) / entryPrice;
        Long openTime = Long.valueOf(trade.getOpentime());

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setProfit(String.format("%.2f", profit * 100));
        tradeDTO.setOpentime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(openTime)));
        tradeDTO.setCurrency(trade.getCurrency());
        return tradeDTO;
    }

    private String getStringRemoveFiat(String moneda) {
        String resultado = moneda.substring(0, moneda.length() - 4);
        return resultado;
    }


}

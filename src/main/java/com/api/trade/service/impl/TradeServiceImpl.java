package com.api.trade.service.impl;

import com.api.trade.domain.Piso;
import com.api.trade.domain.Trade;
import com.api.trade.dto.PisoGenericoDTO;
import com.api.trade.dto.TradeDTO;
import com.api.trade.repository.CurrencyToTrackRepository;
import com.api.trade.repository.PisoRepository;
import com.api.trade.repository.TradeRepository;
import com.api.trade.request.PisosGenerericosRequest;
import com.api.trade.request.VentaRequest;
import com.api.trade.service.TradeService;
import jakarta.inject.Inject;
import modes.Live;
import trading.Currency;
import trading.CurrentAPI;
import com.binance.api.client.domain.market.CandlestickInterval;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeServiceImpl implements TradeService {
    @Inject
    private TradeRepository tradeRepository;
    @Inject
    private CurrencyToTrackRepository currencyToTrackRepository;

    @Inject
    private PisoRepository pisoRepository;

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
    public List<Trade> getPisoAndMoneda(Long piso, String moneda) {
        return null;
    }

    @Override
    public void createTradesGenericos(PisosGenerericosRequest pisosGenerericosRequest) {

        String coin = pisosGenerericosRequest.getCoin();
        String fiat = pisosGenerericosRequest.getFiat();
        String par =  coin + fiat ;
        String cantPisos = pisosGenerericosRequest.getCantidadPisos();
        String intervalo = pisosGenerericosRequest.getIntervalo();
        String periodos = pisosGenerericosRequest.getCantidadPeriodos();

        //Eliminar todos los pisos por par
        pisoRepository.deleteByPair(par);

        //Eliminar todos los trades por par
        tradeRepository.deleteByPair(par);


        //Guardar todos los pisos
        List<Piso> pisos = null;
        List<Trade> trades = null;
        try {
            pisos = Live.getPisos(coin,fiat,cantPisos, intervalo, periodos);
            trades = Live.getTrades(coin,fiat,cantPisos, intervalo, periodos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        pisoRepository.saveAll(pisos);
        tradeRepository.saveAll(trades);

        //Guardar todos los trades
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




    private List<Piso> parsePisos(List<PisoGenericoDTO> pisoGenericoDTOS) {
        List<Piso> pisosResult = new ArrayList<Piso>();
        if (null != pisoGenericoDTOS && !pisoGenericoDTOS.isEmpty()) {
            for (PisoGenericoDTO pisoGenericoDTO: pisoGenericoDTOS) {

                BigDecimal porcentajeTakeProfit = new BigDecimal(pisoGenericoDTO.getPorcentajeTakeProfit()).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal porcentajeBajada = new BigDecimal(pisoGenericoDTO.getPorcentajeBajada()).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal porcentajeInvertido = new BigDecimal(pisoGenericoDTO.getPorcentajeInvertido()).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);

                Piso piso = new Piso();
                piso.setNro(Long.valueOf(pisoGenericoDTO.getNroPiso()));
                piso.setPair(pisoGenericoDTO.getPair());
                piso.setTakeprofit(porcentajeTakeProfit.toString());
                piso.setPorcentajebajada(porcentajeBajada.toString());
                piso.setPorcentajedinero(porcentajeInvertido.toString());
                piso.setMargen(pisoGenericoDTO.getMargen());
                pisosResult.add(piso);
            }
        }
        return pisosResult;
    }

    List<Trade> parseTrades(List<PisoGenericoDTO> pisoGenericoDTOS){
        List<Trade> tradesResult = new ArrayList<Trade>();
        if (null != pisoGenericoDTOS && !pisoGenericoDTOS.isEmpty()) {
            int count = 1;
            for (PisoGenericoDTO pisoGenericoDTO: pisoGenericoDTOS) {
                Trade trade = new Trade();
                trade.setAmount(pisoGenericoDTO.getAmount());
                trade.setCurrency(pisoGenericoDTO.getPair());
                trade.setOpentime(new Date().getTime()+count);
                trade.setCurrentprice(pisoGenericoDTO.getPrecioActual());
                trade.setEntryprice(pisoGenericoDTO.getPrecioEntrada());
                trade.setTotal(pisoGenericoDTO.getTotalDolares());
                trade.setPiso(pisoGenericoDTO.getNroPiso());
                trade.setHigh(pisoGenericoDTO.getPrecioEntrada());
                trade.setLow(pisoGenericoDTO.getPrecioEntrada());
                tradesResult.add(trade);
                count++;
            }
        }
        return tradesResult;
    }


}

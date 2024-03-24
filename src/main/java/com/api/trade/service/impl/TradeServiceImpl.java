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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

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

        //Eliminar todos los pisos por par
        pisoRepository.deleteByPair(pisosGenerericosRequest.getPar());

        //Eliminar todos los trades por par
        tradeRepository.deleteByPair(pisosGenerericosRequest.getPar());

        List<PisoGenericoDTO> pisosGenericos = this.getPisosGenericos(pisosGenerericosRequest);

        //Guardar todos los pisos
        List<Piso> pisos = this.parsePisos(pisosGenericos);
        pisoRepository.saveAll(pisos);

        List<Trade> trades = this.parseTrades(pisosGenericos);
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


    private List<PisoGenericoDTO> getPisosGenericos(PisosGenerericosRequest pisosGenerericosRequest) {
        BigDecimal precioDesde = new BigDecimal(pisosGenerericosRequest.getPrecioDesde());
        BigDecimal precioHasta = new BigDecimal(pisosGenerericosRequest.getPrecioHasta());
        int cantidadPisos = pisosGenerericosRequest.getCantidadPisos();
        BigDecimal cantidadTotalParaRepartirEntrePisos = new BigDecimal(pisosGenerericosRequest.getCantidadTotalParaRepartirEntrePisos());
        BigDecimal precioActual = new BigDecimal(pisosGenerericosRequest.getPrecioActual());
        BigDecimal amountTotal = cantidadTotalParaRepartirEntrePisos.divide(precioActual, 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
        BigDecimal amountTotalVariable = new BigDecimal(amountTotal.toString());
        BigDecimal precioActualMonedaFiat = new BigDecimal(pisosGenerericosRequest.getPrecioActualMonedaFiat());

        List<PisoGenericoDTO> pisos = new ArrayList<>();

        BigDecimal porcentajeBajadaPrecioDesdeHasta = precioHasta.subtract(precioDesde).divide(precioHasta, 5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal porcentajeSubidaPrecioDesdeHasta = precioHasta.subtract(precioDesde).divide(precioDesde, 5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal porcentajeBajadaPorCadaPiso = porcentajeBajadaPrecioDesdeHasta.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal porcentajeTakeProfitPorCadaPiso = porcentajeSubidaPrecioDesdeHasta.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal amountPorPiso = cantidadTotalParaRepartirEntrePisos.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal porcentajeInvertidoPorPiso = new BigDecimal("0");



        BigDecimal precioEntrada = precioHasta;


        for (int i = 1; i <= cantidadPisos; i++) {
            BigDecimal porcentaje = (i == 1) ? BigDecimal.ZERO : porcentajeBajadaPorCadaPiso.negate();
            BigDecimal bajada = precioEntrada.multiply(porcentaje.abs().divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_UP)).setScale(10, BigDecimal.ROUND_HALF_UP);
            precioEntrada = precioEntrada.subtract(bajada).setScale(10, BigDecimal.ROUND_UP);

            BigDecimal amount = new BigDecimal("0");
            BigDecimal total = amount.multiply(precioEntrada);
            if (precioActualMonedaFiat.equals(new BigDecimal("1"))) {
                amount = amountPorPiso.divide(precioEntrada, RoundingMode.HALF_UP);
                porcentajeInvertidoPorPiso = amount.divide(amountTotalVariable, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                total = amount.multiply(precioEntrada);
            } else {
                amount = amountPorPiso;
                BigDecimal precioMonedaContraria = new BigDecimal(pisosGenerericosRequest.getPrecioMonedaContraria());
                total = amount.multiply(precioMonedaContraria);
                porcentajeInvertidoPorPiso = amount.divide(cantidadTotalParaRepartirEntrePisos,10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            }







            PisoGenericoDTO pisoGenericoDTO = new PisoGenericoDTO();
            pisoGenericoDTO.setNroPiso(Long.valueOf(i));
            pisoGenericoDTO.setPorcentajeBajada(porcentaje.toString());
            pisoGenericoDTO.setPrecioEntrada(precioEntrada.toString());
            pisoGenericoDTO.setPorcentajeTakeProfit(porcentajeTakeProfitPorCadaPiso.toString());
            pisoGenericoDTO.setAmount(amount.toString());
            pisoGenericoDTO.setPorcentajeInvertido(porcentajeInvertidoPorPiso.toString());
            pisoGenericoDTO.setPair(pisosGenerericosRequest.getPar());
            pisoGenericoDTO.setPrecioActual(precioActual.toString());
            pisoGenericoDTO.setTotalDolares(total.toString());

            if (pisoGenericoDTO.getNroPiso() == 1) {
                pisoGenericoDTO.setMargen(String.valueOf(pisosGenerericosRequest.getMargen() / 100));
            } else {
                pisoGenericoDTO.setMargen(String.valueOf(0d));
            }
            pisos.add(pisoGenericoDTO);
            amountTotalVariable = amountTotalVariable.subtract(amount);
        }


        return pisos;
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

package com.api.trade.service.impl;

import com.api.trade.dto.TradeDetailsDTO;
import com.api.trade.repository.TradeRepository;
import com.api.trade.service.TradeDetailService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.inject.Inject;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeDetailServiceImpl implements TradeDetailService {
    @Inject
    private TradeRepository tradeRepository;

    @Override
    public List<TradeDetailsDTO> getTradeDetails() {
        String tradesPisosJson = tradeRepository.getTradesPisosJSON();
        JsonArray jsonArray = new JsonParser().parse(tradesPisosJson).getAsJsonArray();
        List<TradeDetailsDTO> tradeDetailsDTOs = new ArrayList<TradeDetailsDTO>();
        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < jsonArray.size(); i++) {
            TradeDetailsDTO tradeDetailsDTO = new TradeDetailsDTO();
            JsonObject jsonObject =  jsonArray.get(i).getAsJsonObject();

            String moneda = jsonObject.get("pair").getAsString();
            double cTakeProfit = jsonObject.get("takeprofit").getAsDouble();
            double margen = jsonObject.get("margen").getAsDouble();
            double porcentajeBajada = jsonObject.get("porcentajebajada").getAsDouble();
            double porcentajeDinero = jsonObject.get("porcentajedinero").getAsDouble();
            Long idPiso = jsonObject.get("idpiso").getAsLong();
            double takeProfit = cTakeProfit * 100;
            double porcentajeEntradaContraPisoAnterior = porcentajeBajada * 100;
            double porcentajeInvertido = porcentajeDinero * 100;
            double porcentajeMargen = margen * 100;
            Long piso = jsonObject.get("nro").getAsLong();

            tradeDetailsDTO.setInvertido(Double.valueOf(df.format(porcentajeInvertido)));
            tradeDetailsDTO.setPorcentajeEntrada(Double.valueOf(df.format(porcentajeEntradaContraPisoAnterior)));
            tradeDetailsDTO.setTakeProfit(Double.valueOf(df.format(takeProfit)));
            tradeDetailsDTO.setMoneda(moneda);
            tradeDetailsDTO.setIdPiso(idPiso);
            tradeDetailsDTO.setPiso(piso.longValue());
            tradeDetailsDTO.setMargen(porcentajeMargen);

            boolean idTradeNotNull = !jsonObject.get("idtrade").isJsonNull();

            if(idTradeNotNull){
                boolean isCurrentPriceNotNull = !jsonObject.get("currentprice").isJsonNull();
                double currentPrice = 0;
                if(isCurrentPriceNotNull){
                    currentPrice = jsonObject.get("currentprice").getAsDouble();
                } else {
                    currentPrice = jsonObject.get("entryprice").getAsDouble();
                }

                double entryPrice = jsonObject.get("entryprice").getAsDouble();
                double high = jsonObject.get("high").getAsDouble();
                double low = jsonObject.get("low").getAsDouble();


                Long openTime = jsonObject.get("opentime").getAsLong();
                Date openTimeDate = new Date(openTime);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String fecha = sdf.format(openTimeDate);

                double porcentajeActual = Double.valueOf((currentPrice - entryPrice) / entryPrice * 100.0D);
                double porcentajeHigh = Double.valueOf((high - entryPrice) / entryPrice * 100.0D);
                double porcentajeLow = Double.valueOf((low - entryPrice) / entryPrice * 100.0D);

                tradeDetailsDTO.setPorcentajeActual(Double.valueOf(df.format(porcentajeActual)));
                tradeDetailsDTO.setPorcentajeMaximo(Double.valueOf(df.format(porcentajeHigh)));
                tradeDetailsDTO.setPorcentajeMinimo(Double.valueOf(df.format(porcentajeLow)));
                tradeDetailsDTO.setPrecioEntrada(Double.valueOf(df.format(entryPrice)));
                tradeDetailsDTO.setFecha(fecha);
            }
            tradeDetailsDTOs.add(tradeDetailsDTO);
        }

        return tradeDetailsDTOs;
    }
}

package com.api.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeDetailsDTO {

    private Long idTrade;
    private Long idPiso;
    private Long piso;
    private Double porcentajeActual;
    private Double porcentajeMinimo;
    private Double porcentajeMaximo;
    private Double porcentajeEntrada;
    private Double precioEntrada;
    private Double takeProfit;
    private Double invertido;
    private String moneda;
    private String fecha;
    private Double margen;


}

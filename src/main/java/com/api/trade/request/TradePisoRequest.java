package com.api.trade.request;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Introspected
@NoArgsConstructor
@Builder
public class TradePisoRequest {
    private Long idPiso;
    private double takeProfit;
    private double porcentajeBajada;
    private double porcentajeInvertido;
    private double margen;
    private Long nro;
    private String moneda;
}

package com.api.trade.request;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@Introspected
@NoArgsConstructor
@Builder
public class PisosGenerericosRequest {
    private String precioDesde;
    private String precioHasta;
    private String precioActual;
    private String precioActualMonedaFiat;
    private String precioMonedaContraria;
    private String cantidadTotalParaRepartirEntrePisos;
    private int cantidadPisos;
    private String par;
    private double margen;
}

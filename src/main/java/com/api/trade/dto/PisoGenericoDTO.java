package com.api.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PisoGenericoDTO {
    private Long nroPiso;
    private String porcentajeBajada;
    private String precioEntrada;
    private String precioActual;
    private String porcentajeTakeProfit;
    private String amount;
    private String porcentajeInvertido;
    private String pair;
    private String margen;
    private String totalDolares;
}
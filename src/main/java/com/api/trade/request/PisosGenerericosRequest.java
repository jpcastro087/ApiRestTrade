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
    private String cantidadPisos;
    private String coin;
    private String fiat;
    private String intervalo;
    private String cantidadPeriodos;

}

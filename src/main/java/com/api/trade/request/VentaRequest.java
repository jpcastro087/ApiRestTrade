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
public class VentaRequest {
    private Long idPiso;
    private double porcentaje;
}

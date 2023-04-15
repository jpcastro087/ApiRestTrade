package com.api.trade.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "monedavolumen")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonedaVolumen {
    @Id
    private Long id;

    @Column(name = "moneda", nullable = false)
    private String moneda;

    @Column(name = "porcmayorvolumen", nullable = false)
    private String porcHastaMayorVolumen;

    @Column(name = "porcmenorvolumen", nullable = true)
    private String porcHastaMenorVolumen;

}

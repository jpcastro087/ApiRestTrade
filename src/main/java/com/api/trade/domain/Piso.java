package com.api.trade.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pisos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Piso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    @Column(name = "nro", nullable = false)
    private Long nro;

    @Column(name = "porcentajebajada", nullable = true)
    private String porcentajebajada;

    @Column(name = "porcentajedinero", nullable = false)
    private String porcentajedinero;

    @Column(name = "takeprofit", nullable = true)
    private String takeprofit;

    @Column(name = "margen", nullable = true)
    private String margen;

    @Column(name = "pair", nullable = false)
    private String pair;
}

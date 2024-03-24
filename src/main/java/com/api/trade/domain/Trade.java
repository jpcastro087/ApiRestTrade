package com.api.trade.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "trade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    @Column(name = "opentime", nullable = false)
    private Long opentime;

    @Column(name = "closetime", nullable = true)
    private Long closetime;

    @Column(name = "entryprice", nullable = false)
    private String entryprice;

    @Column(name = "closeprice", nullable = true)
    private String closeprice;

    @Column(name = "currentprice", nullable = true)
    private String currentprice;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "total", nullable = false)
    private String total;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "high", nullable = true)
    private String high;

    @Column(name = "low", nullable = true)
    private String low;

    @Column(name = "piso", nullable = true)
    private Long piso;
}

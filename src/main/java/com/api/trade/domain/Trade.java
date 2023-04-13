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
    private Long id;

    @Column(name = "opentime", nullable = false)
    private String opentime;

    @Column(name = "closetime", nullable = true)
    private String closetime;

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
}

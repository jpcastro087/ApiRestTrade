package com.api.trade.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "currenciestotrack")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyToTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    @Column(name = "moneda", nullable = false)
    private String moneda;
}

package com.api.trade.repository;

import com.api.trade.domain.CurrencyToTrack;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyToTrackRepository extends CrudRepository<CurrencyToTrack, Long> {

    @Query(value = "select moneda from currenciestotrack where moneda = :moneda", nativeQuery = true)
    Optional<String> getByMoneda(String moneda);

}

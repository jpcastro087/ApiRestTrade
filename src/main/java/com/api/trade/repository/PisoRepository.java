package com.api.trade.repository;

import com.api.trade.domain.Piso;
import com.api.trade.domain.Trade;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface PisoRepository extends CrudRepository<Piso, Long> {


    @Query(value = "select max(nro) from pisos p where p.pair = :moneda", nativeQuery = true)
    Optional<BigInteger> getPisoMaximoByMoneda(String moneda);

    @Query(value = "select * from pisos p where p.pair = :pair", nativeQuery = true)
    Optional<List<Piso>> getByPair(String pair);

}

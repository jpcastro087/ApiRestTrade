package com.api.trade.repository;

import com.api.trade.domain.Trade;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends CrudRepository<Trade,Long> {

    @Query(value = "select * from trade t where t.closetime is null and currency = :pair", nativeQuery = true)
    Trade getOpenTrade(String pair);

    @Query(value = " select * from ( " +
            " select t.*, (cast(currentPrice as decimal) - cast(entryPrice as decimal) ) / cast(entryPrice as decimal) * 100 profit from trade t where t.closetime is null " +
            " ) a order by profit desc", nativeQuery = true)
    List<Trade> getActiveTrades();


    @Query(value = "SELECT getText()", nativeQuery = true)
    String getTradesPisosJSON();

    @Query(value = "select * from trade t where t.piso = :piso and t.currency = :moneda", nativeQuery = true)
    Optional<List<Trade>> getByPisoAndMoneda(Long piso, String moneda);

    @Transactional
    @Query("DELETE FROM Trade t WHERE t.currency = :currency")
    void deleteByPair(String currency);


}

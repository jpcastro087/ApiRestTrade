package com.api.trade.repository;

import com.api.trade.domain.MonedaVolumen;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface MonedaVolumenRepository extends CrudRepository<MonedaVolumen, Long> {

}
package com.api.trade.repository;

import com.api.trade.domain.Piso;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface PisoRepository extends CrudRepository<Piso, Long> {
}

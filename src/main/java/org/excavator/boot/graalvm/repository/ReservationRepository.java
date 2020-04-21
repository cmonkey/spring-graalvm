package org.excavator.boot.graalvm.repository;

import org.excavator.boot.graalvm.entity.Reservation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReservationRepository extends ReactiveCrudRepository<Reservation, Integer> {
}

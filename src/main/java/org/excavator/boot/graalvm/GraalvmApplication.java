package org.excavator.boot.graalvm;

import lombok.extern.log4j.Log4j2;
import org.excavator.boot.graalvm.entity.Reservation;
import org.excavator.boot.graalvm.repository.ReservationRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@SpringBootApplication(proxyBeanMethods = false)
public class GraalvmApplication {

	@Bean
	RouterFunction<ServerResponse> routes(ReservationRepository reservationRepository){
		return route().GET("/reservations", r -> ok().body(reservationRepository.findAll(), Reservation.class))
				.build();
	}

	@Bean
	ApplicationRunner runner(DatabaseClient databaseClient, ReservationRepository reservationRepository){
		return args -> {
			Flux<Reservation> flux = Flux.just("cmonkey", "foo")
					.map(name -> new Reservation(null , name))
					.flatMap(reservationRepository::save);

			databaseClient.execute("create table reservation(id identity , name varchar (20))")
					.fetch()
					.rowsUpdated()
					.thenMany(flux)
					.thenMany(reservationRepository.findAll())
					.subscribe(log::info);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(GraalvmApplication.class, args);
	}

}

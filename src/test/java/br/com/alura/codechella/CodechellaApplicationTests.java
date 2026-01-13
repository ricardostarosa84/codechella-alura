package br.com.alura.codechella;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CodechellaApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void cadastraNovoEvento() {
		EventoDto dto = new EventoDto(null, TipoEvento.SHOW, "Kiss",
				LocalDate.parse("2025-01-01"),
				"Show da melhor banda que existe");

		webTestClient.post().uri("/eventos")
				.bodyValue(dto)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(EventoDto.class)
				.value(response -> {
					assertNotNull(response.id());
					assertEquals(dto.tipo(), response.tipo());
					assertEquals(dto.nome(), response.nome());
					assertEquals(dto.data(), response.data());
					assertEquals(dto.descricao(), response.descricao());
				});
	}

	@Test
	void buscarEventos() {
		EventoDto dto = new EventoDto(3L, TipoEvento.CONCERTO, "Concerto de Verão",
				LocalDate.parse("2024-06-10"),
				"Um concerto ao ar livre com grandes nomes da música clássica.");

		webTestClient.get().uri("/eventos")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(EventoDto.class)
				.value(response -> {
					EventoDto eventoDto = response.get(2);

					assertEquals(dto.id(), eventoDto.id());
					assertEquals(dto.tipo(), eventoDto.tipo());
					assertEquals(dto.nome(), eventoDto.nome());
					assertEquals(dto.data(), eventoDto.data());
					assertEquals(dto.descricao(), eventoDto.descricao());
				});
	}
}

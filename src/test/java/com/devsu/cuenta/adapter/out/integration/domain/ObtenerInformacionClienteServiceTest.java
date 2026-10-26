package com.devsu.cuenta.adapter.out.integration.domain;

import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;
import com.devsu.cuenta.exception.IntegrationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
class ObtenerInformacionClienteServiceTest {

    public static MockWebServer mockWebServer;

    private ObtenerInformacionClientePort obtenerInformacionClientePort;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void init() {
        var baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        obtenerInformacionClientePort = new ObtenerInformacionClienteService(webClient);
    }

    @Test
    void obtenerInformacionClientePorIdentificacionClienteTest() throws Exception {
        var cliente = new ObtenerInformacionClientePort.ClienteDto(1L, "1234", "Cliente");
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "id": 1,
                            "identificacion": "1234",
                            "nombre": "Cliente"
                        }
                        """)
                .addHeader("Content-Type", "application/json"));
        var clienteResponse = obtenerInformacionClientePort.obtenerInformacionCliente("1234");
        StepVerifier.create(clienteResponse)
                .expectNextMatches(clienteDto -> clienteDto.nombre().equals(cliente.nombre()))
                .verifyComplete();
    }

    @Test
    void obtenerInformacionClientePorIdentificacionClienteSinValorTest() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "codigo": 99,
                            "mensaje": "Error test"
                        }
                        """)
                .addHeader("Content-Type", "application/json")
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
        );
        var clienteResponse = obtenerInformacionClientePort.obtenerInformacionCliente("1234");
        StepVerifier.create(clienteResponse)
                .expectErrorMatches(error ->
                        error instanceof IntegrationException && error.getMessage().equals("Error test"))
                .verify();
    }

}

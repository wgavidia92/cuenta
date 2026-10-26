package com.devsu.cuenta.adapter.out.integration.domain;

import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.IntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
class ObtenerInformacionClienteService implements ObtenerInformacionClientePort {

    private final WebClient webClient;

    ObtenerInformacionClienteService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${integration.clientes.uri}")
    private String uriPath;

    @Override
    public Mono<ClienteDto> obtenerInformacionCliente(String identificacion) throws CuentaException {
        return webClient.get()
                .uri(uriPath, identificacion)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(Error.class)
                                .map(error -> new IntegrationException(error.mensaje()))
                )
                .bodyToMono(ObtenerInformacionClientePort.ClienteDto.class)
                .timeout(Duration.ofSeconds(5))
                ;
    }

    record Error(String mensaje) {
    }

}

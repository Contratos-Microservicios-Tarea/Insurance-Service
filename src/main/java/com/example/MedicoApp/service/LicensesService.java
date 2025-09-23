package com.example.MedicoApp.service;

import org.springframework.stereotype.Service;

import com.example.MedicoApp.dto.LicenceDto;
import com.example.MedicoApp.dto.VerifyResponseDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;


@Service
public class LicensesService {

    public LicensesService(WebClient licenciasWebClient) {
        this.webClient = licenciasWebClient;
    }

    private final WebClient webClient;

    // Consulta todas las licencias de un paciente
    public Mono<List<LicenceDto>> getLicenciasByPaciente(String patientId) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/licenses")
                        .queryParam("patientId", patientId)
                        .build())
                .retrieve()
                .bodyToFlux(LicenceDto.class)
                .collectList();
    }

    // Verifica si una licencia existe y está issued
    public VerifyResponseDto verifyLicencia(String folio) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/licenses/{folio}/verify")
                        .build(folio))
                .retrieve()
                .bodyToMono(VerifyResponseDto.class)
                .block();
    }

}

package com.example.MedicoApp.controller;

import com.example.MedicoApp.dto.VerifyResponseDto;
import com.example.MedicoApp.service.LicensesService;
import org.springframework.web.bind.annotation.*;

import com.example.MedicoApp.dto.LicenceDto;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/insurer/licenses/")
public class InsuranceController {

    LicensesService insuranceService;

    @GetMapping("{folio}/verify")
    protected final VerifyResponseDto verificarFolio(@PathVariable String folio) {
        System.out.println("folio: " + folio);

        return insuranceService.verifyLicencia(folio);
    }

    @GetMapping("{patientId}/licenses")
    protected final Mono<List<LicenceDto>> getLicecncias(@PathVariable String patientId) {
        return insuranceService.getLicenciasByPaciente(patientId) ;
    }

    @GetMapping("")
    protected final String test() {
        return "test";
    }
}

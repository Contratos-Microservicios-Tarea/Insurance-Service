package com.example.pact;

import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.MedicoApp.dto.LicenceDto;
import com.example.MedicoApp.service.LicensesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@PactTestFor(providerName = "Licencias", port = "8081")
@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest (classes = com.example.MedicoApp.PactApplication.class)
@TestPropertySource(properties = "LICENSES_URL=http://localhost:8081")
class InsurerPactConsumerTest {

    @Autowired
    private LicensesService licensesService;


    // 🔹 obtener licencias por paciente
    @Pact(consumer = "Validador-Aseguradora")
    public V4Pact getLicensesForPatient(PactDslWithProvider builder) {
        PactDslJsonBody license = new PactDslJsonBody()
                .stringType("folio", "L-1001")
                .stringValue("patientId", "11111111-1")
                .stringValue("doctorId", "D-123")
                .stringType("diagnosis")
                .date("startDate", "yyyy-MM-dd")
                .integerType("days", 7)
                .stringValue("status", "issued");

        return builder
                .given("patient 11111111-1 has issued license folio L-1001")
                .uponReceiving("get licenses for patient")
                .path("/licenses")
                .query("patientId=11111111-1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body((license))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getLicensesForPatient")
    void testGetLicensesForPatient() {
        Mono<List<LicenceDto>> result = licensesService.getLicenciasByPaciente("11111111-1");
        List<LicenceDto> licenses = result.block();
        assertNotNull(licenses);
        assertFalse(licenses.isEmpty());
    }


    // 🔹 obtener licencias por paciente que no tiene licencia
    @Pact(provider = "Licencias", consumer = "Validador-Aseguradora")
    public V4Pact getLicensesForPatientWithoutLicense(PactDslWithProvider builder) {
        PactDslJsonArray emptyArray = new PactDslJsonArray();

        return builder
                .given("patient 22222222-2 has no licenses")
                .uponReceiving("get licenses for patient without licenses")
                .path("/licenses")
                .query("patientId=22222222-2")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(emptyArray)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getLicensesForPatientWithoutLicense")
    void testGetLicensesForPatientWithoutLicense() {
        Mono<List<LicenceDto>> result = licensesService.getLicenciasByPaciente("22222222-2");
        List<LicenceDto> licenses = result.block();
        assertNotNull(licenses);
        assertTrue(licenses.isEmpty());
    }
}

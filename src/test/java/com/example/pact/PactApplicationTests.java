package com.example.pact;

import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.MedicoApp.dto.LicenceDto;
import com.example.MedicoApp.dto.VerifyResponseDto;
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


    @Pact(consumer = "Validador-Aseguradora")
    public V4Pact verifyLicenseValid(PactDslWithProvider builder) {
        PactDslJsonBody response = new PactDslJsonBody()
                .stringValue("folio", "L-1001")
                .booleanValue("valid", true)
                .stringValue("status", "issued");

        return builder
                .given("folio L-1001 exists and is issued")
                .uponReceiving("verify valid license folio")
                .path("/licenses/L-1001/verify")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(response)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "verifyLicenseValid")
    void testVerifyLicenciaValid() {
        VerifyResponseDto response = licensesService.verifyLicencia("L-1001");
        assertNotNull(response);
        assertEquals(true, response.valid());
    }



    @Pact(consumer = "Validador-Aseguradora")
    public V4Pact verifyLicenseInvalid(PactDslWithProvider builder) {
        PactDslJsonBody response = new PactDslJsonBody()
                .stringValue("folio", "L-9999")
                .booleanValue("valid", false)
                .stringValue("status", "not_found");

        return builder
                .given("folio L-9999 does not exist")
                .uponReceiving("verify invalid license folio")
                .path("/licenses/L-9999/verify")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(response)
                .toPact(V4Pact.class);
    }


    @Test
    @PactTestFor(pactMethod = "verifyLicenseInvalid")
    void testVerifyLicenciaInvalid() {
        VerifyResponseDto response = licensesService.verifyLicencia("L-9999");
        assertNotNull(response);
        assertEquals(false, response.valid());
    }
}

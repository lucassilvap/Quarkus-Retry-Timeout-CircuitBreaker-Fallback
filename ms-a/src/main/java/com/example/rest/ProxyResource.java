package com.example.rest;

import com.example.client.MsBClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.smallrye.faulttolerance.api.CircuitBreakerName;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;

@Path("/call-b")
public class ProxyResource {

    @Inject
    @RestClient
    MsBClient msBClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 3, delay = 500)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 3000)
    @Fallback(fallbackMethod = "fallbackCall")
    public String callMsB() {
        return "Resposta do ms-b : " + msBClient.hello();
    }

    // Fallback caso ms-b esteja fora
    public String fallbackCall() {
        return "ms-b está indisponível no momento (fallback ativado)";
    }
}

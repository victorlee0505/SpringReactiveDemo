package com.example.webflux.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.demo.service.BuildInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class HealthController implements HealthApi {

    private final BuildInfo buildInfo;

    @Override
    public Mono<ResponseEntity<String>> health(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(buildInfo.getVersionString()));
    }

    /**
     * GET / : ping end point for app service keep awake
     *
     * @return Successful response (status code 200)
     */
    @Operation(
        operationId = "ping",
        summary = "App Service Always On end point",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/",
        produces = { "application/json" }
    )
    
    public Mono<ResponseEntity<String>> ping() {
        return Mono.just(ResponseEntity.ok("OK"));
    }
    
}

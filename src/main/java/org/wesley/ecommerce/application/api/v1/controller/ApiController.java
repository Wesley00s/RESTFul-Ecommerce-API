package org.wesley.ecommerce.application.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.ApiInfoResponse;

@RestController
@Tag(name = "API Controller", description = "RESTFul API for display API info.")
@RequestMapping("/v1")
public class ApiController {
    @GetMapping
    @Operation(
            summary = "Display API info",
            description = "Display information related the API"
    )
    public ResponseEntity<ApiInfoResponse> apiInfo() {
        return ResponseEntity.ok(
                new ApiInfoResponse(
                        "Products Services API",
                        "API to products and carts management",
                        "1.0",
                        "",
                        "Wesley Rodrigues de Sousa",
                        "wesley300rodrigues@gmail.com",
                        "https://github.com/Wesley00s"
                )
        );
    }
}

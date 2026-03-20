package com.gateway.payment.controller;

import com.gateway.payment.dto.CustomerRequest;
import com.gateway.payment.dto.CustomerResponse;
import com.gateway.payment.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @PathVariable Long companyId,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.create(companyId, request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(customerService.findByCompanyId(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long companyId,
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<CustomerResponse> block(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.block(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse> activate(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.activate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
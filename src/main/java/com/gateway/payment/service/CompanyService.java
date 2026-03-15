package com.gateway.payment.service;

import com.gateway.payment.dto.AddressRequest;
import com.gateway.payment.dto.CompanyRequest;
import com.gateway.payment.dto.CompanyResponse;
import com.gateway.payment.entity.Address;
import com.gateway.payment.entity.Company;
import com.gateway.payment.entity.User;
import com.gateway.payment.enums.CompanyStatus;
import com.gateway.payment.exception.BusinessException;
import com.gateway.payment.exception.ResourceNotFoundException;
import com.gateway.payment.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;

    @Transactional
    public CompanyResponse create(Long userId, CompanyRequest request) {
        User user = userService.findById(userId);

        if (companyRepository.existsByDocument(request.getDocument())) {
            throw new BusinessException("CNPJ já cadastrado");
        }

        Company company = Company.builder()
                .user(user)
                .name(request.getName())
                .tradeName(request.getTradeName())
                .document(request.getDocument())
                .responsibleName(request.getResponsibleName())
                .responsibleDocument(request.getResponsibleDocument())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(buildAddress(request.getAddress()))
                .status(CompanyStatus.PENDING)
                .build();

        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public CompanyResponse findById(Long id) {
        return CompanyResponse.fromEntity(getCompanyById(id));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> findByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .stream()
                .map(CompanyResponse::fromEntity)
                .toList();
    }

    @Transactional
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = getCompanyById(id);

        company.setName(request.getName());
        company.setTradeName(request.getTradeName());
        company.setResponsibleName(request.getResponsibleName());
        company.setResponsibleDocument(request.getResponsibleDocument());
        company.setPhone(request.getPhone());
        company.setEmail(request.getEmail());

        if (request.getAddress() != null) {
            if (company.getAddress() != null) {
                updateAddress(company.getAddress(), request.getAddress());
            } else {
                company.setAddress(buildAddress(request.getAddress()));
            }
        }

        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    @Transactional
    public CompanyResponse approve(Long id) {
        Company company = getCompanyById(id);
        company.setStatus(CompanyStatus.ACTIVE);
        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    @Transactional
    public CompanyResponse suspend(Long id) {
        Company company = getCompanyById(id);
        company.setStatus(CompanyStatus.SUSPENDED);
        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    @Transactional
    public void delete(Long id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada: " + id));
    }

    private Address buildAddress(AddressRequest req) {
        if (req == null) return null;
        return Address.builder()
                .street(req.getStreet())
                .number(req.getNumber())
                .complement(req.getComplement())
                .neighborhood(req.getNeighborhood())
                .city(req.getCity())
                .state(req.getState())
                .zipCode(req.getZipCode())
                .country(req.getCountry() != null ? req.getCountry() : "BR")
                .build();
    }

    private void updateAddress(Address address, AddressRequest req) {
        address.setStreet(req.getStreet());
        address.setNumber(req.getNumber());
        address.setComplement(req.getComplement());
        address.setNeighborhood(req.getNeighborhood());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setZipCode(req.getZipCode());
        if (req.getCountry() != null) address.setCountry(req.getCountry());
    }
}
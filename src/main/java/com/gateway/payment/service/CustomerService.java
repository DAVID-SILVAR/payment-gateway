package com.gateway.payment.service;

import com.gateway.payment.dto.CustomerRequest;
import com.gateway.payment.dto.CustomerResponse;
import com.gateway.payment.dto.AddressRequest;
import com.gateway.payment.entity.Address;
import com.gateway.payment.entity.Company;
import com.gateway.payment.entity.Customer;
import com.gateway.payment.enums.CustomerStatus;
import com.gateway.payment.exception.BusinessException;
import com.gateway.payment.exception.ResourceNotFoundException;
import com.gateway.payment.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyService companyService;

    @Transactional
    public CustomerResponse create(Long companyId, CustomerRequest request) {
        Company company = companyService.getCompanyById(companyId);

        if (customerRepository.existsByCompanyIdAndDocument(companyId, request.getDocument())) {
            throw new BusinessException("Cliente com este documento já existe nesta empresa");
        }

        Customer customer = Customer.builder()
                .company(company)
                .name(request.getName())
                .email(request.getEmail())
                .document(request.getDocument())
                .documentType(request.getDocumentType())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .status(CustomerStatus.ACTIVE)
                .address(buildAddress(request.getAddress()))
                .build();

        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return CustomerResponse.fromEntity(getCustomerById(id));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findByCompanyId(Long companyId) {
        return customerRepository.findByCompanyId(companyId)
                .stream()
                .map(CustomerResponse::fromEntity)
                .toList();
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = getCustomerById(id);

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setBirthDate(request.getBirthDate());
        customer.setDocumentType(request.getDocumentType());

        if (request.getAddress() != null) {
            if (customer.getAddress() != null) {
                updateAddress(customer.getAddress(), request.getAddress());
            } else {
                customer.setAddress(buildAddress(request.getAddress()));
            }
        }

        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse block(Long id) {
        Customer customer = getCustomerById(id);
        customer.setStatus(CustomerStatus.BLOCKED);
        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse activate(Long id) {
        Customer customer = getCustomerById(id);
        customer.setStatus(CustomerStatus.ACTIVE);
        return CustomerResponse.fromEntity(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    public Customer getCustomerById(Long id) {
        try {
            Optional<Customer> customer = customerRepository.findById(id);

            if (customer.isPresent()) {
                return customer.get();
            } else {
                throw new ResourceNotFoundException("Cliente não encontrado: " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente com ID: " + id, e);
        }
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

        if (req.getCountry() != null) {
            address.setCountry(req.getCountry());
        }
    }
}
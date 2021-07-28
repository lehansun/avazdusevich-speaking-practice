package com.lehansun.pet.project.model.dto;

import com.lehansun.pet.project.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExtendedSecureCustomerDTO extends SecureCustomerDTO {

    private List<Role> roles;
}

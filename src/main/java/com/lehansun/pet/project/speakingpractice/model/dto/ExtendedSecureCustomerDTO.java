package com.lehansun.pet.project.speakingpractice.model.dto;


import com.lehansun.pet.project.speakingpractice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ExtendedSecureCustomerDTO extends SecureCustomerDTO {

    private List<Role> roles;
}

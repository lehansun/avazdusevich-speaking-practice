package com.lehansun.pet.project.model.dto;

import com.lehansun.pet.project.model.Country;
import com.lehansun.pet.project.model.Language;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateOfBirth;
    private Language nativeLanguage;
    private Language learningLanguage;
    private Country country;
}

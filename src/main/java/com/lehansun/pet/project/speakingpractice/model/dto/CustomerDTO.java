package com.lehansun.pet.project.speakingpractice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDTO implements Comparable<CustomerDTO> {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateOfBirth;
    private LanguageDTO nativeLanguage;
    private LanguageDTO learningLanguage;
    private CountryDTO country;

    @Override
    public int compareTo(CustomerDTO customerDTO) {
        return this.getUsername().compareTo(customerDTO.getUsername());
    }
}

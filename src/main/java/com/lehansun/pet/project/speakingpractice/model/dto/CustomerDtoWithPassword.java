package com.lehansun.pet.project.speakingpractice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDtoWithPassword {

    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateOfBirth;
    private LanguageDTO nativeLanguage;
    private LanguageDTO learningLanguage;
    private CountryDTO country;

}

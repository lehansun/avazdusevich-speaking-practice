package com.lehansun.pet.project.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestDTO {

    private long id;

    private CustomerDTO initiatedBy;
    private CustomerDTO acceptedBy;

    private LanguageDTO requestedLanguage;

    private LocalDate wishedStartTime;
    private LocalDate wishedEndTime;
    private LocalDate acceptedStartTime;
}

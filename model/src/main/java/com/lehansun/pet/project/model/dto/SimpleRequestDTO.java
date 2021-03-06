package com.lehansun.pet.project.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleRequestDTO {
    private LocalDate wishedStartTime;
    private LocalDate wishedEndTime;
}

package com.lehansun.pet.project.speakingpractice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
@Data
public class Country extends BaseEntity {

    @Column(name = "name")
    private String name;

}

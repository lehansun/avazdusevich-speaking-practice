package com.lehansun.pet.project.model;

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

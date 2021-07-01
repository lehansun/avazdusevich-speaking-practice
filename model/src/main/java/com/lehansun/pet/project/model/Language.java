package com.lehansun.pet.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "languages")
public class Language extends BaseEntity {

    @Column(name = "name")
    private String name;

}

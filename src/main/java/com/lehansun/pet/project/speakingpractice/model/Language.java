package com.lehansun.pet.project.speakingpractice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "languages")
@Data
public class Language extends BaseEntity {

    @Column(name = "name")
    private String name;

}

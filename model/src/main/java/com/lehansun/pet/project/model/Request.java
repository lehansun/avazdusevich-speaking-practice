package com.lehansun.pet.project.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "requests")
public class Request extends BaseEntity {

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private Customer initiatedBy;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "acceptor_id")
    private Customer acceptedBy;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    private Language requestedLanguage;

    @Column(name = "wished_start_time")
    private LocalDate wishedStartTime;
    @Column(name = "wished_end_time")
    private LocalDate wishedEndTime;
    @Column(name = "accepted_start_time")
    private LocalDate acceptedStartTime;
}

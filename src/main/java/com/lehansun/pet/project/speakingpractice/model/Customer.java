package com.lehansun.pet.project.speakingpractice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customer_roles",
            joinColumns = {@JoinColumn(name = "customer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "native_language_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Language nativeLanguage;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "learning_language_id")
    private Language learningLanguage;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nativeLanguage=" + nativeLanguage +
                ", learningLanguage=" + learningLanguage +
                ", country='" + country + '\'' +
                '}';
    }
}

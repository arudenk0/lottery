package com.nexign.lottery.entities;

import com.nexign.lottery.dtos.ParticipantDto;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Name must be not blank")
    private String name;
    @Range(message = "Must be between 18 and 120", min = 18, max = 120)
    private int age;
    @NotBlank(message = "City must be not blank")
    private String city;

    public Participant(){

    }

    public Participant(Participant participant) {
        this.name = participant.getName();
        this.age = participant.getAge();
        this.city = participant.getCity();
    }

    public Participant(ParticipantDto dto) {
        this.name = dto.getName();
        this.age = dto.getAge();
        this.city = dto.getCity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

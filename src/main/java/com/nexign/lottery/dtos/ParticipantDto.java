package com.nexign.lottery.dtos;

import com.nexign.lottery.entities.Participant;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

public class ParticipantDto {
    @NotBlank(message = "Name must be not blank")
    private String name;
    @Range(message = "Must be between 18 and 120", min = 18, max = 120)
    private int age;
    @NotBlank(message = "City must be not blank")
    private String city;

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

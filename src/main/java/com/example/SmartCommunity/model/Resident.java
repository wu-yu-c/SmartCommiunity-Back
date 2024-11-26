package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Resident")
public class Resident {
    @Id
    @Column(name = "ResidentID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ResidentName", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "ResidentAddress", nullable = false)
    private String address;

    @Size(max = 50)
    @Column(name = "ResidentPhoneNumber", length = 50)
    private String phoneNumber;

}
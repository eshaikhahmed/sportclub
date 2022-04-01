package com.fun.sportclub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sport")
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @Column(name = "sport_name")
    private String sportName;

    @Column(name = "sport_image1")
    private String sportImage1;

    @Column(name = "sport_image2")
    private String sportImage2;

    @Column(name = "sport_description")
    private String sportDescription;

}
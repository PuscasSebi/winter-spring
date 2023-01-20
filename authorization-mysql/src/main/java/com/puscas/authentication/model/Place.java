package com.puscas.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "creator")
    public String placeCreator;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String descriptionName;

    @Column(name = "image")
    public String placeImage;

}

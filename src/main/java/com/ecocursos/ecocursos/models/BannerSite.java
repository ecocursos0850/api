package com.ecocursos.ecocursos.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "banner_site")
@Data
public class BannerSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String caminho;

}

package com.example.demo.entity;

import com.example.demo.service.EncryptService;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String urlOriginal;

    @Column(unique = true, nullable = false)
    private String urlShort;

    public Url(String urlOriginal, String urlShort) {
        this.urlOriginal = urlOriginal;
        this.urlShort = urlShort;
    }
}
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "urlview")
@Getter
@Setter
@NoArgsConstructor
public class UrlView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    private String urlOriginal;

    private String date;


    public UrlView(String urlShort, String string, String urlOriginal) {
        this.url = urlShort;
        this.urlOriginal = urlOriginal;
        this.date = string;
    }
}
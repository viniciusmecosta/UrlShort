package com.example.demo.repository;

import com.example.demo.entity.Url;

public interface UrlRepositoryEncrypt {
    Url findByDecryptedOriginalUrl(String urlOriginal);
}

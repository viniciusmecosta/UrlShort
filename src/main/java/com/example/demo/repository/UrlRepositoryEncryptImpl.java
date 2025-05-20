package com.example.demo.repository;
import com.example.demo.entity.Url;
import com.example.demo.service.EncryptService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UrlRepositoryEncryptImpl implements UrlRepositoryEncrypt {

    @PersistenceContext
    private EntityManager entityManager;

    private EncryptService encryptService;


    @Override
    public Url findByDecryptedOriginalUrl(String urlOriginal) {
        List<Url> urls = entityManager.createQuery("SELECT u FROM Url u", Url.class).getResultList();
        for (Url url : urls) {
            String decrypted = EncryptService.decryptAES(url.getUrlOrignal());
            if (urlOriginal.equals(decrypted)) {
                return url;
            }
        }
        return null;
    }
}

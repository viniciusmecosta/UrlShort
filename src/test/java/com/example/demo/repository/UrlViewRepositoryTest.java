package com.example.demo.repository;

import com.example.demo.entity.UrlView;
import com.example.demo.to.UrlRankingTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UrlViewRepositoryTest {

    @Autowired
    private UrlViewRepository urlViewRepository;

    @Test
    void save_shouldPersistUrlView() {
        UrlView urlView = new UrlView("https://abc123.com","https://urllong.com","2025-01-20" );
        UrlView savedUrlView = urlViewRepository.save(urlView);

        assertNotNull(savedUrlView);
        assertEquals("https://abc123.com", savedUrlView.getUrl());
        assertEquals("2025-01-20", savedUrlView.getDate());
    }

    @Test
    void find_allReturnsListUrlRankingTO(){
        UrlView urlView = new UrlView("url1","url1longa", "2025");
        UrlView urlView1 = new UrlView("url2","url2longa", "2024");
        UrlView urlView2 = new UrlView("url3","url3longa", "2023");
        UrlView urlView3 = new UrlView("url4","url4longa", "2022");
        urlViewRepository.save(urlView);
        urlViewRepository.save(urlView1);
        urlViewRepository.save(urlView2);
        urlViewRepository.save(urlView3);

        List<UrlRankingTO> urlRankingTO = urlViewRepository.findRankingUrlView();
        assertNotNull(urlRankingTO);
        System.out.println(urlRankingTO);
        assertEquals(4, urlRankingTO.size());
        assertEquals("url1", urlRankingTO.get(0).urlShort());
        assertEquals("url4", urlRankingTO.get(3).urlShort());
    }
}

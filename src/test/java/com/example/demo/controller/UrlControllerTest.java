package com.example.demo.controller;

import com.example.demo.exception.NoUrlViewException;
import com.example.demo.exception.UrlInvalidException;
import com.example.demo.exception.UrlNotFoundException;
import com.example.demo.exception.UrlNullException;
import com.example.demo.to.UrlRankingTO;
import com.example.demo.service.UrlService;
import com.example.demo.to.UrlResponseTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    void create_Returns201WithShortenedUrl() throws Exception {
        String originalUrl = "https://example.com";
        String shortUrl = "https://abc123.com";
        UrlResponseTO url = new UrlResponseTO(originalUrl, shortUrl);

        when(urlService.shortenUrl(originalUrl)).thenReturn(url);

        mockMvc.perform(post("/api/create")
                        .param("url", originalUrl))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.urlOriginal").value(originalUrl))
                .andExpect(jsonPath("$.urlShort").value(shortUrl));

        verify(urlService).shortenUrl(originalUrl);
    }

    @Test
    void create_Returns412WhenUrlIsNull() throws Exception {
        String originalUrl = "";

        when(urlService.shortenUrl(originalUrl)).thenThrow(new UrlNullException("URL null"));

        mockMvc.perform(post("/api/create")
                        .param("url", originalUrl))
                .andExpect(status().isPreconditionFailed());

        verify(urlService).shortenUrl(originalUrl);
    }

    @Test
    void create_Returns400WhenUrlIsInvalid() throws Exception {
        String originalUrl = "urlinvalid";

        when(urlService.shortenUrl(originalUrl)).thenThrow(new UrlInvalidException("Invalid URL (Example: 'example.com' or 'https://example.com' or 'http://example.com')"));

        mockMvc.perform(post("/api/create")
                        .param("url", originalUrl))
                .andExpect(status().isBadRequest());

        verify(urlService).shortenUrl(originalUrl);
    }

    @Test
    void find_Redirect() throws Exception {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";

        when(urlService.find(shortUrl)).thenReturn(originalUrl);

        mockMvc.perform(get("/api/find")
                        .param("url", shortUrl))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", URI.create(originalUrl).toString()));

        verify(urlService).find(shortUrl);
    }

    @Test
    void find_Returns404WhenUrlNotFound() throws Exception {
        String shortUrl = "https://example.com";

        when(urlService.find(shortUrl)).thenThrow(new UrlNotFoundException("Url not found"));

        mockMvc.perform(get("/api/find")
                        .param("url", shortUrl))
                .andExpect(status().isNotFound());
        verify(urlService).find(shortUrl);
    }

    @Test
    void find_Returns400WhenUrlIsInvalid() throws Exception {
        String shortUrl = "abc123";

        when(urlService.find(shortUrl)).thenThrow(new UrlInvalidException("Invalid URL (Example: 'example.com' or 'https://example.com' or 'http://example.com')"));

        mockMvc.perform(get("/api/find")
                        .param("url", shortUrl))
                .andExpect(status().isBadRequest());
        verify(urlService).find(shortUrl);
    }

    @Test
    void ranking_Returns200WithListOfUrls() throws Exception {
        Long count1 = 5L;
        Long count2 = 3L;
        List<UrlRankingTO> ranking = List.of(
                new UrlRankingTO("https://example1.com", count1),
                new UrlRankingTO("https://example2.com", count2)
        );

        when(urlService.ranking()).thenReturn(ranking);

        mockMvc.perform(get("/api/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("https://example1.com"))
                .andExpect(jsonPath("$[0].count").value(count1))
                .andExpect(jsonPath("$[1].url").value("https://example2.com"))
                .andExpect(jsonPath("$[1].count").value(count2));

        verify(urlService).ranking();
    }

    @Test
    void ranking_Returns404WhenNoUrlsExist() throws Exception {
        when(urlService.ranking()).thenThrow(new NoUrlViewException("No URL view available"));

        mockMvc.perform(get("/api/ranking"))
                .andExpect(status().isNotFound());

        verify(urlService).ranking();
    }
}
package com.example.demo.repository;

import com.example.demo.entity.UrlView;
import com.example.demo.to.UrlRankingTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlViewRepository extends JpaRepository<UrlView, Long> {

    @Query("SELECT new com.example.demo.to.UrlRankingTO(n.url, n.urlOriginal, COUNT(n)) FROM UrlView n GROUP BY n.url ORDER BY COUNT(n) DESC")
    List<UrlRankingTO> findRankingUrlView();
}
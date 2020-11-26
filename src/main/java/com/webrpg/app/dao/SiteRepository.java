package com.webrpg.app.dao;

import com.webrpg.app.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site,Long> {
    public List<Site> findAll();
    public Page<Site> findAll(Pageable pageable);
    public List<Site> findByName(String name);
    public List<Site> findById(long id);
    public List<Site> findAllById(long id);
    public List<Site> deleteById(long id);
    public boolean existsByName(String name);
    public long count();

}
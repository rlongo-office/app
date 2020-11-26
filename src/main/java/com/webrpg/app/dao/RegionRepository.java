package com.webrpg.app.dao;

import com.webrpg.app.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region,Long> {
public List<Region> findAll();
public Page<Region> findAll(Pageable pageable);
public List<Region> findByName(String name);
public List<Region> findByType(String type);
public List<Region> findById(long id);
public List<Region> findAllById(long id);
public List<Region> deleteById(long id);
public boolean existsByName(String name);
public long count();

        }


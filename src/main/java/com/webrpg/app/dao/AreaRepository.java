package com.webrpg.app.dao;

import com.webrpg.app.model.Area;
import com.webrpg.app.model.Thing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area,Long> {
    public List<Area> findAll();
    public Page<Area> findAll(Pageable pageable);
    public List<Area> findByName(String name);
    public List<Area> findById(long id);
    public List<Area> findAllById(long id);
    public List<Area> deleteById(long id);
    public boolean existsByName(String name);
    public List<Area> findAllByType(long id);
    public long count();

}

package com.webrpg.app.dao;

import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Thingdef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingdefRepository extends JpaRepository<Thingdef,Long> {
    public List<Thingdef> findAll();
    public Page<Thingdef> findAll(Pageable pageable);
    public List<Thingdef> findByName(String name);
    public List<Thingdef> findById(long id);
    public List<Thingdef> findAllById(long id);
    public List<Thingdef> deleteById(long id);
    public boolean existsByName(String name);
    public long count();
}
package com.webrpg.app.dao;

import com.webrpg.app.model.Thing;
import com.webrpg.app.model.Thingdef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingRepository extends JpaRepository<Thing,Long> {
    public List<Thing> findAll();
    public Page<Thing> findAll(Pageable pageable);
    public List<Thing> findByName(String name);
    public List<Thing> findById(long id);
    public List<Thing> findAllById(long id);
    public List<Thing> deleteById(long id);
    public boolean existsByName(String name);
    public long count();

}


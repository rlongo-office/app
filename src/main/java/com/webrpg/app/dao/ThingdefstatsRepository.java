package com.webrpg.app.dao;

import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Thingdef;
import com.webrpg.app.model.Thingdefstats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingdefstatsRepository extends JpaRepository<Thingdefstats,Long> {
    public List<Thingdefstats> findAll();
    public Page<Thingdefstats> findAll(Pageable pageable);

    public Thingdefstats findById(long id);
    public Thingdefstats deleteById(long id);
    public long count();
    public Thingdefstats save(Thingdefstats a);

    public List<Thingdefstats> findByThingdefByIdThingdef_Id(Long tdID);

}
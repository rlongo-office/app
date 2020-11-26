package com.webrpg.app.dao;

import com.webrpg.app.model.Thingdefstatsview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingdefstatsviewRepository extends JpaRepository<Thingdefstatsview,Long> {
    public List<Thingdefstatsview> findAll();
    public Page<Thingdefstatsview> findAll(Pageable pageable);
    public List<Thingdefstatsview>  findByDefname(String name);
    public List<Thingdefstatsview> findByDefid(int id);
    public Thingdefstatsview findById(int id);
    // Full query
    @Query("SELECT t FROM Thingdefstatsview t WHERE t.id = :id ORDER BY t.attrid ASC")
    public List<Thingdefstatsview> findAllOrdered();
    //public Thingdefstatsview deleteById(long id); not allowing
    //public long count(); redundant
}

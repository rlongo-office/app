package com.webrpg.app.dao;
import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Powerdef;
import com.webrpg.app.model.Thingdef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerdefRepository extends JpaRepository<Powerdef,Long> {
    public List<Powerdef> findAll();
    public Page<Powerdef> findAll(Pageable pageable);
    public List<Powerdef> findByName(String name);
    public List<Powerdef> findById(long id);
    public List<Powerdef> findAllById(long id);
    public List<Powerdef> deleteById(long id);
    public boolean existsByName(String name);
    public long count();
}

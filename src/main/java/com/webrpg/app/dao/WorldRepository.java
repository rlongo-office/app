package com.webrpg.app.dao;

import com.webrpg.app.model.Area;
import com.webrpg.app.model.World;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorldRepository extends JpaRepository<World,Long> {
    public List<World> findAll();
    public Page<World> findAll(Pageable pageable);
    public List<World> findByName(String name);
    public List<World> findById(long id);
    public List<World> findAllById(long id);
    public List<World> deleteById(long id);
    public boolean existsByName(String name);
    public List<World> findAllByType(long id);
    public long count();

}
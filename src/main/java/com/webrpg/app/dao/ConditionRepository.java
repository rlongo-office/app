package com.webrpg.app.dao;

import com.webrpg.app.model.ActivityLog;
import com.webrpg.app.model.Area;
import com.webrpg.app.model.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionRepository extends JpaRepository<Condition,Long> {
    public List<Condition> findAll();
    public Page<Condition> findAll(Pageable pageable);
    public List<Condition> findByName(String name);
    public List<Condition> findById(long id);
    public List<Condition> findAllById(long id);
    public List<Condition> deleteById(long id);
    public boolean existsByName(String name);
    public List<Condition> findByType(long id);
    //Need a method to return messages that contain a specific string and ignore message
    public List<Condition> findByDescriptionContainingIgnoreCase(String content);
    public long count();

}

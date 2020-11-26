package com.webrpg.app.dao;

import com.webrpg.app.model.Activity;
import com.webrpg.app.model.Powerdef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    public List<Activity> findAll();
    public Page<Activity> findAll(Pageable pageable);
    public List<Activity> findByName(String name);
    public List<Activity> findByStartDate(Integer elapsedTime);
    public List<Activity> findByEndDate(Integer elapsedTime);
    public List<Activity> findByStartTime(Integer intraDay);
    public List<Activity> findByEndTime(Integer intraDay);
    public List<Activity> findByRecurrence(Integer recCode);
    public List<Activity> findById(long id);
    public List<Activity> findAllById(long id);
    public List<Activity> deleteById(long id);
    public boolean existsByName(String name);
    public long count();
}
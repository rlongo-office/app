package com.webrpg.app.dao;

import com.webrpg.app.model.Schedule;
import com.webrpg.app.model.Thing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    public List<Schedule> findAll();
    public Page<Schedule> findAll(Pageable pageable);
    public List<Schedule> findByName(String name);
    public List<Schedule> findById(long id);
    public List<Schedule> findAllById(long id);
    public List<Schedule> deleteById(long id);
    public boolean existsByName(String name);
    public long count();

}
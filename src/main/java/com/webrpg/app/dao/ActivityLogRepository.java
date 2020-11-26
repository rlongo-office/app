package com.webrpg.app.dao;

import com.webrpg.app.model.ActivityLog;
import com.webrpg.app.model.Thing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {
    public List<ActivityLog> findAll();
    public Page<ActivityLog> findAll(Pageable pageable);
    public List<ActivityLog> findById(long id);
    public List<ActivityLog> findAllById(long id);
    public List<ActivityLog> deleteById(long id);
    public long count();
    //Need a method to return messages that contain a specific string and ignore message
    public List<ActivityLog> findByMessageContainingIgnoreCase(String content);
    //Need a method that returns messages between certain game times
    public List<ActivityLog> findByGameTimeBetween(Long startTime, Long EndTime);

}
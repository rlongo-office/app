package com.webrpg.app.dao;


import com.webrpg.app.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.security.Timestamp;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    public List<History> findAll();
    public Page<History> findAll(Pageable pageable);
    public List<History> findById(long id);
    public List<History> findAllById(long id);
    public List<History> deleteById(long id);
    public long count();

    //Need a method to return messages that contain a specific string and ignore message
    public List<History> findByMessageContainingIgnoreCase(String content);

    //Need a method that returns messages between certain game times and
    public List<History> findByGameTimeBetween(Long startTime, Long EndTime);
    public List<History> findAllBySysTimeBetween(Timestamp publicationTimeStart,
            Timestamp publicationTimeEnd);
    //Pulled from a Baeldung post on querying for times before/after a time
    //@Query("select a from History a where a.sysTime <= :messageDateTime")
    public List<History> findAllBySysTimeBefore(Timestamp messageDateTime);
    public List<History> findAllByGameTimeLessThan(Long messageGameTime);

}
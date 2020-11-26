package com.webrpg.app.dao;

import com.webrpg.app.model.Storyline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorylineRepository extends JpaRepository<Storyline,Long> {
    public List<Storyline> findAll();
    public Page<Storyline> findAll(Pageable pageable);
    public List<Storyline> findById(long id);
    public List<Storyline> findAllById(long id);
    public List<Storyline> deleteById(long id);
    public long count();
    //Need a method to return messages that contain a specific string and ignore message
    public List<Storyline> findByDescriptionContainingIgnoreCase(String content);
    //Need a method that returns messages between certain game times
    public List<Storyline> findByBeginTimeBetween(Long startTime, Long EndTime);
    public List<Storyline> findByEndTimeBetween(Long startTime, Long EndTime);
}
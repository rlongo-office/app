package com.webrpg.app.dao;

import com.webrpg.app.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location,Long> {
    public List<Location> findAll();
    public Page<Location> findAll(Pageable pageable);
    public List<Location> findByName(String name);
    public List<Location> findById(long id);
    public List<Location> findAllById(long id);
    public List<Location> deleteById(long id);
    //IN the future we can explore using Specification object and passed tha instead of method naming convention
    public List<Location> findAllByXcoordinateAndYcoordinateAndZcoordinate(Long xcoord, Long ycoord, Long zcoord);
    public boolean existsByName(String name);
    public long count();

}
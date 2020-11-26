package com.webrpg.app.dao;

import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Thingdefstatsview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Long> {
    @Query("SELECT t FROM Attribute t ORDER BY t.id ASC")
    @Override
    public List<Attribute> findAll();
    public List<Attribute> findAll(Sort sort);
    public List<Attribute> findAllByOrderByIdAsc();
    //records were not coming back ordered, forcing this using Query override below
    @Query("SELECT t FROM Attribute t ORDER BY t.id ASC")
    @Override
    public Page<Attribute> findAll(Pageable pageable);
    public List<Attribute> findAllById(long id);
    public List<Attribute> findByName(String name);
    public List<Attribute> findById(long id);
    public List<Attribute> deleteById(long id);
    public long count();

    /*
    We are allowing Attribute records to share the same name, but their uniqueness comes from the id as the PK value.  However, as a point of
    logical organization of the information, we are not allowing an Attribute to have both the same name AND the same ParentID.  So assuming we know the
    parent category, say, an attribute called 'Condition', then we can look for something like the Attribute 'Sleep' under 'Condition', and not, say, 'Sleep'
    under a parent called 'Spells'.  But this proved to be a tricky query because 'parent id' is a reflexive foreign key that CAN be null but that relationship
    is represented as an Attribute object within the Attribute Entity class (as a ManytoOne) and I couldn't find a simple way to compare a null database column value
    with a null object in the entity (that I could find).  So we need to test for two conditions.  ONE, parentID parameter is not null and equals
    The id field of the Object within Attribute (called attributeByIdAttribute), and TWO, we expect the parentID column value AND the attributeByIdAttribute member
     are null.  Either condition is sufficient to return a true for that test of the parentID for our "exists" query.  This may be useful for other cases
    where we need to test uniqueness against a table with multiple columns, where some of those columns could be null.
     */
    @Query(value = "SELECT CASE WHEN COUNT(a) > 0 THEN 'true' ELSE 'false' END FROM Attribute a WHERE "
            + "a.name = :name " +
            "AND ((a.attributeByIdAttribute.id = :parentID) or (a.attributeByIdAttribute IS NULL AND :parentID IS NULL))")
    public boolean existByNameAndParentID(@Param("name") String name, @Param("parentID")Long parentID);
    public Attribute save(Attribute a);
}

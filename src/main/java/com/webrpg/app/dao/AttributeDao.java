package com.webrpg.app.dao;

import com.webrpg.app.model.Attribute;

import java.util.List;

public interface AttributeDao {
    public List<Attribute> getAttributesByPage(int pageNum, int recordCount);
    public List<Attribute> getAttributesByPage(int pageNum, int recordCount, int method);
}

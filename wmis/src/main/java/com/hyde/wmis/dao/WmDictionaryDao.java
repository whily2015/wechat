package com.hyde.wmis.dao;

import com.hyde.wmis.entity.WmDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WmDictionaryDao extends JpaRepository<WmDictionary, Integer> {

    WmDictionary findByDictTypeAndDictKeyOrderBySort(String type, String key);
}

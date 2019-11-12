package com.hyde.wmis.service;

import com.hyde.wmis.dao.WmDictionaryDao;
import com.hyde.wmis.entity.WmDictionary;
import com.hyde.wmis.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class WmDictionaryService {
    @Resource
    private WmDictionaryDao wmDictionaryDao;

    public WmDictionary findWmDictionary(String type, String key) {
        return wmDictionaryDao.findByDictTypeAndDictKeyOrderBySort(type, key);
    }

    @Transactional
    public void saveWmDictionary(WmDictionary dictionary) {
        dictionary.setLastUpdateTime(DateUtil.getDate());
        wmDictionaryDao.save(dictionary);
    }
}

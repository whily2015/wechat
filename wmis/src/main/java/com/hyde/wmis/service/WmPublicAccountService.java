package com.hyde.wmis.service;

import com.hyde.wmis.dao.WmPublicAccountDao;
import com.hyde.wmis.entity.WmPublicAccount;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WmPublicAccountService {
    @Resource
    private WmPublicAccountDao wmPublicAccountDao;

    public List<WmPublicAccount> findAll() {
        return wmPublicAccountDao.findAll();
    }

}

package com.hyde.wmis.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "wm_public_account")
public class WmPublicAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String paType;
    private String paName;
    private String wechatNo;
    private String appId;
    private String secret;
    private String paDescribe;
    private Date lastUpdateTime;
    private Date createTime;
}

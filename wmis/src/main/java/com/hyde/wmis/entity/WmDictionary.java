package com.hyde.wmis.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "wm_dictionary")
public class WmDictionary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dictType;
    private String dictName;
    private String dictKey;
    private String dictValue;
    private Integer sort;
    private Date lastUpdateTime;
    private Date createTime;
}

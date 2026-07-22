package com.github.aqiu202.starters.jpa.entity;

import com.github.aqiu202.starters.jpa.id.SnowflakeId;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class KeyEntity {

    @Id
    @SnowflakeId
    @Column(length = 30)
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

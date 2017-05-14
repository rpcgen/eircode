package com.fexco.cache;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CacheEntry {

    private String key;
    private String value;
    private String statusCode;

    public CacheEntry() {
        // JPA required
    }

    public CacheEntry(String key, String value, String statusCode) {
        this.key        = key;
        this.value      = value;
        this.statusCode = statusCode;
    }

    @Id
    @Column(columnDefinition="varchar2(1024)")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(columnDefinition="CLOB")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

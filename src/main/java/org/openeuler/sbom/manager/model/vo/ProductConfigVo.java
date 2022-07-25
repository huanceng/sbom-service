package org.openeuler.sbom.manager.model.vo;

import java.io.Serializable;

public class ProductConfigVo implements Serializable {

    private String name;

    private String valueType;

    private Integer ord;

    public ProductConfigVo(String name, String valueType, Integer ord) {
        this.name = name;
        this.valueType = valueType;
        this.ord = ord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }
}

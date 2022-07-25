package org.openeuler.sbom.manager.model.vo;

import java.io.Serializable;

public class ProductConfigVo implements Serializable {

    private String name;

    private String label;

    private String valueType;

    private Integer ord;

    public ProductConfigVo(String name, String label, String valueType, Integer ord) {
        this.name = name;
        this.label = label;
        this.valueType = valueType;
        this.ord = ord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

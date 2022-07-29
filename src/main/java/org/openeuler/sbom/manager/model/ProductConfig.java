package org.openeuler.sbom.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Config of a specific productType.
 */
@Entity
@Table(indexes = {
        @Index(name = "name_product_type_uk", columnList = "name, product_type", unique = true),
        @Index(name = "product_type_idx", columnList = "product_type")
})
public class ProductConfig {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Name of a config.
     *  version: 1.7.0/1.8.0/20.03/22.03/...
     *  platform: cpu/gpu/ascend/...
     *  os: linux/windows/macos/...
     *  arch: x86_64/aarch64/...
     *  language: Python 3.7.0/Python 3.8.0/...
     *  imageType: ios/docker/source/...
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * 前端展示用字段名称
     * */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String label;

    /**
     * Value type of config, such as Integer/String/Boolean/enum(x86_64, aarch64).
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String valueType;

    /**
     * Order of a config.
     */
    @Column(nullable = false)
    private Integer ord;

    /**
     * Product type that the config belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_type", foreignKey = @ForeignKey(name = "product_type_fk"))
    @JsonIgnore
    private ProductType productType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
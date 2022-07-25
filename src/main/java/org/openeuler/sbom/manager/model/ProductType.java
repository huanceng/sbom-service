package org.openeuler.sbom.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Product type, must be openeuler/mindspore/opengauss
 */
@Entity
public class ProductType {
    @Id
    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    /**
     * Product configs of a product type.
     */
    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductConfig> productConfigs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProductConfig> getProductConfigs() {
        return productConfigs;
    }

    public void setProductConfigs(List<ProductConfig> productConfigs) {
        this.productConfigs = productConfigs;
    }
}
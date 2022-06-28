package org.openeuler.sbom.manager.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(indexes = {
        // TODO 后续product功能完成后去除productName
        @Index(name = "raw_sbom_uk", columnList = "spec, spec_version, format, productName", unique = true)
})
public class RawSbom {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String spec;

    @Column(columnDefinition = "TEXT", name = "spec_version", nullable = false)
    private String specVersion;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String format;

    @Column(columnDefinition = "BYTEA", nullable = false)
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] value;

    // @OneToOne(mappedBy = "rawSbom", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    // private Product product;
    // TODO 待后续product功能完成后，productName切换成product对象
    private String productName;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    private Timestamp updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

//    public Product getProduct() {
//        return product;
//    }

//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}

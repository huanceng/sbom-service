package org.openeuler.sbom.manager.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Map;
import java.util.UUID;

/**
 * Describes a product.
 */
@Entity
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Table(indexes = {
        @Index(name = "name_uk", columnList = "name", unique = true),
        @Index(name = "attr_uk", columnList = "attribute", unique = true)
})
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Name of a product.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

//    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Sbom sbom;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RawSbom> rawSboms;

    /**
     * Attributes of a product.
     */
    @Column(columnDefinition = "JSONB", nullable = false)
    @Type(type = "jsonb")
    private Map<String, ?> attribute;

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

//    public List<Sbom> getSboms() {
//        return sboms;
//    }
//
//    public void setSboms(List<Sbom> sboms) {
//        this.sboms = sboms;
//    }

//    public List<RawSbom> getRawSboms() {
//        return rawSboms;
//    }
//
//    public void setRawSboms(List<RawSbom> rawSboms) {
//        this.rawSboms = rawSboms;
//    }

    public Map<String, ?> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, ?> attribute) {
        this.attribute = attribute;
    }
}


package org.openeuler.sbom.manager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

/**
 * Describes a product.
 */
@Entity
@Table(indexes = {
        @Index(name = "name_version_uk", columnList = "name, version", unique = true)
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

    /**
     * Version of a product.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String version;

//    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Sbom sbom;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RawSbom> rawSboms;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}

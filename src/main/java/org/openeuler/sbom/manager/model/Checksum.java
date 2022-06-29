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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Checksum of a package.
 */
@Entity
@Table(indexes = {
        @Index(name = "checksum_uk", columnList = "pkg_id, algorithm, value", unique = true),
        @Index(name = "pkg_id_idx", columnList = "pkg_id")
})
public class Checksum {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Algorithm used to produce the checksum.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String algorithm;

    /**
     * Checksum value produced using a specific algorithm.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pkg_id", foreignKey = @ForeignKey(name = "pkg_id_fk"))
    private Package pkg;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }
}

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
 * External vulnerability reference of a package.
 */
@Entity
@Table(indexes = {
        @Index(name = "package_vul_uk", columnList = "pkg_id, vul_id", unique = true)
})
public class ExternalVulRef {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Category for the external reference.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String category;

    /**
     * Type of the external reference, may be 'cve', etc.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    /**
     * Status of a vulnerability in a package.
     */
    @Column(columnDefinition = "TEXT")
    private String status;

    /**
     * Human-readable information about the purpose and target of the reference.
     */
    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * Vulnerability of the reference.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vul_id", foreignKey = @ForeignKey(name = "vul_id_fk"))
    private Vulnerability vulnerability;

    /**
     * Package of the reference.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pkg_id", foreignKey = @ForeignKey(name = "pkg_id_fk"))
    private Package pkg;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }
}

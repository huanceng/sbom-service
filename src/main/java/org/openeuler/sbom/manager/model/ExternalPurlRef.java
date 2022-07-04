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
 * External purl reference of a package.
 */
@Entity
@Table(indexes = {
        @Index(name = "external_purl_ref_uk", columnList = "pkg_id, category, type, purl", unique = true)
})
public class ExternalPurlRef {
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
     * Type of the external reference.
     * Here, the type MUST be 'purl'.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    /**
     * Human-readable information about the purpose and target of the reference.
     */
    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * Purl of the reference.
     */
    @Column(columnDefinition = "TEXT")
    private String purl;

    /**
     * Package of the reference.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pkg_id", foreignKey = @ForeignKey(name = "pkg_id_fk"))
    @JsonIgnore
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }
}

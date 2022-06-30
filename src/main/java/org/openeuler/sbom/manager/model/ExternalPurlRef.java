package org.openeuler.sbom.manager.model;

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
        @Index(name = "package_purl_uk", columnList = "pkg_id, purl_id", unique = true)
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

    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * Foreign key that refers to a purl id.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purl_id", foreignKey = @ForeignKey(name = "purl_id_fk"))
    private Purl purl;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Purl getPurl() {
        return purl;
    }

    public void setPurl(Purl purl) {
        this.purl = purl;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }
}

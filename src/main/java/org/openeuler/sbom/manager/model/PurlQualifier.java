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
 * Describes a package url qualifier.
 */
@Entity
@Table(indexes = {
        @Index(name = "purl_qualifier_uk", columnList = "purl_id, key, value", unique = true),
        @Index(name = "purl_id_idx", columnList = "purl_id")
})
public class PurlQualifier {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Key of a qualifier.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String key;

    /**
     * Value of a qualifier.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    /**
     * Purl that a qualifier belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purl_id", foreignKey = @ForeignKey(name = "purl_id_fk"))
    private Purl purl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Purl getPurl() {
        return purl;
    }

    public void setPurl(Purl purl) {
        this.purl = purl;
    }
}

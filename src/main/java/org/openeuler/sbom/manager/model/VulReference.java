package org.openeuler.sbom.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Describes a vulnerability reference.
 */
@Entity
@Table(indexes = {
        @Index(name = "vul_id_idx", columnList = "vul_id")
})
public class VulReference {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Reference source, e.g., NVD, snyk, github
     */
    @Column(columnDefinition = "TEXT")
    private String source;

    /**
     * The URI pointing to detail of this vulnerability. This can also be used to derive the source of this information.
     */
    @Column(columnDefinition = "TEXT")
    private String url;

    /**
     * Vulnerability described by the reference.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vul_id", foreignKey = @ForeignKey(name = "vul_id_fk"))
    @JsonIgnore
    private Vulnerability vulnerability;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }
}

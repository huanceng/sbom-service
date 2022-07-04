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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Describes a package url.
 */
@Entity
@Table(indexes = {
        @Index(name = "purl_uk", columnList = "external_purl_ref_id, name, namespace, type, version, subpath, qualifier", unique = true),
        @Index(name = "external_purl_ref_id_idx", columnList = "external_purl_ref_id")
})
public class Purl {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Name of a package.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * Namespace of a package, e.g., a Maven groupid, a Docker image owner, a Github user or organization.
     */
    @Column(columnDefinition = "TEXT")
    private String namespace;

    /**
     * Package type or protocol, e.g., maven, npm, pypi, github
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    /**
     * Version of a package.
     */
    @Column(columnDefinition = "TEXT")
    private String version;

    /**
     * Extra subpath within a package.
     */
    @Column(columnDefinition = "TEXT", name = "subpath")
    private String subPath;

    /**
     * Extra qualifying data for a package, e.g., an OS architecture, a distro.
     */
    @Column(columnDefinition = "TEXT")
    private String qualifier;

    @Column(columnDefinition = "TEXT")
    private String purl;

    /**
     * Individual qualifier key-value pairs for a package.
     */
    @OneToMany(mappedBy = "purl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurlQualifier> purlQualifiers;

    /**
     * External purl references that refer to this purl.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "external_purl_ref_id", foreignKey = @ForeignKey(name = "external_purl_ref_id_fk"))
    @JsonIgnore
    private ExternalPurlRef externalPurlRef;

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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public List<PurlQualifier> getPurlQualifiers() {
        return purlQualifiers;
    }

    public void setPurlQualifiers(List<PurlQualifier> purlQualifiers) {
        if (Objects.isNull(this.purlQualifiers)) {
            this.purlQualifiers = purlQualifiers;
        } else {
            this.purlQualifiers.clear();
            this.purlQualifiers.addAll(purlQualifiers);
        }
    }

    public ExternalPurlRef getExternalPurlRef() {
        return externalPurlRef;
    }

    public void setExternalPurlRef(ExternalPurlRef externalPurlRef) {
        this.externalPurlRef = externalPurlRef;
    }
}

package org.openeuler.sbom.manager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "purl_uk", columnList = "name, namespace, type, version, subpath, qualifier", unique = true)
})
public class Purl {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String namespace;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String version;

    @Column(columnDefinition = "TEXT", name = "subpath")
    private String subPath;

    @Column(columnDefinition = "TEXT")
    private String qualifier;

    @OneToMany(mappedBy = "purl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurlQualifier> purlQualifiers;

    @OneToMany(mappedBy = "purl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalPurlRef> externalPurlRefs;

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

    public List<PurlQualifier> getPurlQualifiers() {
        return purlQualifiers;
    }

    public void setPurlQualifiers(List<PurlQualifier> purlQualifiers) {
        this.purlQualifiers = purlQualifiers;
    }

    public List<ExternalPurlRef> getExternalPurlRefs() {
        return externalPurlRefs;
    }

    public void setExternalPurlRefs(List<ExternalPurlRef> externalPurlRefs) {
        this.externalPurlRefs = externalPurlRefs;
    }
}

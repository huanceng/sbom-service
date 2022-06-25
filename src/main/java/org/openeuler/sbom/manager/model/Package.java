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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "purl_uk", columnList = "name, namespace, type, version, subpath, qualifier", unique = true)
})
public class Package {
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

    @Column(columnDefinition = "TEXT")
    private String supplier;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String copyright;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String homepage;

    @Column(columnDefinition = "TEXT", name = "spdx_id")
    private String spdxId;

    @Column(columnDefinition = "TEXT", name = "download_location")
    private String downloadLocation;

    @Column(name = "files_analyzed")
    private boolean filesAnalyzed;

    @Column(columnDefinition = "TEXT", name = "license_concluded")
    private String licenseConcluded;

    @Column(columnDefinition = "TEXT", name = "license_declared")
    private String licenseDeclared;

    @Column(columnDefinition = "TEXT", name = "source_info")
    private String sourceInfo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pkg_license_relp",
            joinColumns = {@JoinColumn(name = "package_id", foreignKey = @ForeignKey(name = "package_id_fk"))},
            inverseJoinColumns = {@JoinColumn(name = "license_id", foreignKey = @ForeignKey(name = "license_id_fk"))})
    private Set<License> licenses;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checksum> checksums;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalVulRef> externalVulRefs;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalPurlRef> externalPurlRefs;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "sbom_id", foreignKey = @ForeignKey(name = "sbom_id_fk"))
    private Sbom sbom;

    @OneToOne(mappedBy = "pkg", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private PkgVerfCode pkgVerfCode;

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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getSpdxId() {
        return spdxId;
    }

    public void setSpdxId(String spdxId) {
        this.spdxId = spdxId;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public boolean isFilesAnalyzed() {
        return filesAnalyzed;
    }

    public void setFilesAnalyzed(boolean filesAnalyzed) {
        this.filesAnalyzed = filesAnalyzed;
    }

    public String getLicenseConcluded() {
        return licenseConcluded;
    }

    public void setLicenseConcluded(String licenseConcluded) {
        this.licenseConcluded = licenseConcluded;
    }

    public String getLicenseDeclared() {
        return licenseDeclared;
    }

    public void setLicenseDeclared(String licenseDeclared) {
        this.licenseDeclared = licenseDeclared;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public Set<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(Set<License> licenses) {
        this.licenses = licenses;
    }

    public List<Checksum> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<Checksum> checksums) {
        this.checksums = checksums;
    }

    public List<ExternalVulRef> getExternalVulRefs() {
        return externalVulRefs;
    }

    public void setExternalVulRefs(List<ExternalVulRef> externalVulRefs) {
        this.externalVulRefs = externalVulRefs;
    }

    public List<ExternalPurlRef> getExternalPurlRefs() {
        return externalPurlRefs;
    }

    public void setExternalPurlRefs(List<ExternalPurlRef> externalPurlRefs) {
        this.externalPurlRefs = externalPurlRefs;
    }

    public Sbom getSbom() {
        return sbom;
    }

    public void setSbom(Sbom sbom) {
        this.sbom = sbom;
    }

    public PkgVerfCode getPkgVerfCode() {
        return pkgVerfCode;
    }

    public void setPkgVerfCode(PkgVerfCode pkgVerfCode) {
        this.pkgVerfCode = pkgVerfCode;
    }
}

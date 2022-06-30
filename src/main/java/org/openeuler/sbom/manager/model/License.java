package org.openeuler.sbom.manager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

/**
 * Describes a license.
 */
@Entity
@Table(indexes = {
        @Index(name = "license_name_uk", columnList = "name", unique = true)
})
public class License {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Name of a license.
     * Multiple names may refer to the same license, e.g., both "Apache 2" and "Apache 2.0" refer to "Apache-2.0".
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * Unique identifier of a license, e.g., "Apache-2.0", "BSD-3-Clause", "GPL-3.0-only".
     */
    @Column(columnDefinition = "TEXT", name = "spdx_license_id")
    private String spdxLicenseId;

    /**
     * Url of a license.
     */
    @Column(columnDefinition = "TEXT")
    private String url;

    @ManyToMany(mappedBy = "licenses")
    private Set<Package> packages;

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

    public String getSpdxLicenseId() {
        return spdxLicenseId;
    }

    public void setSpdxLicenseId(String spdxLicenseId) {
        this.spdxLicenseId = spdxLicenseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Package> getPackages() {
        return packages;
    }

    public void setPackages(Set<Package> packages) {
        this.packages = packages;
    }
}

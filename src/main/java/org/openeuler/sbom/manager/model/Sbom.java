package org.openeuler.sbom.manager.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "spec_name_uk", columnList = "spec, spec_version, name", unique = true)
})
public class Sbom {
    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String spec;

    @Column(columnDefinition = "TEXT", name = "spec_version", nullable = false)
    private String specVersion;

    @Column(columnDefinition = "TEXT", name = "data_license")
    private String dataLicense;

    @Column(columnDefinition = "TEXT")
    private String namespace;

    @Column(columnDefinition = "TEXT")
    private String created;

    @Column(columnDefinition = "TEXT", name = "license_list_version")
    private String licenseListVersion;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_id_fk"))
//    private Product product;

    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;

    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SbomCreator> sbomCreators;

    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SbomElementRelationship> sbomElementRelationships;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getDataLicense() {
        return dataLicense;
    }

    public void setDataLicense(String dataLicense) {
        this.dataLicense = dataLicense;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLicenseListVersion() {
        return licenseListVersion;
    }

    public void setLicenseListVersion(String licenseListVersion) {
        this.licenseListVersion = licenseListVersion;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public List<SbomCreator> getSbomCreators() {
        return sbomCreators;
    }

    public void setSbomCreators(List<SbomCreator> sbomCreators) {
        this.sbomCreators = sbomCreators;
    }

    public List<SbomElementRelationship> getSbomElementRelationships() {
        return sbomElementRelationships;
    }

    public void setSbomElementRelationships(List<SbomElementRelationship> sbomElementRelationships) {
        this.sbomElementRelationships = sbomElementRelationships;
    }
}

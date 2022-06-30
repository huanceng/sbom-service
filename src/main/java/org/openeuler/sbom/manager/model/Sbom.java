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

/**
 * Describes a sbom document.
 */
@Entity
@Table(indexes = {
        @Index(name = "product_id_uk", columnList = "product_id", unique = true)
})
public class Sbom {

    public Sbom() {
    }

    public Sbom(String productId) {
        this.productId = productId;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Name of a sbom document.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * The data license of a sbom document.
     */
    @Column(columnDefinition = "TEXT", name = "data_license")
    private String dataLicense;

    /**
     * A URI provides an unambiguous mechanism for other sbom documents to reference sbom elements within this sbom document.
     */
    @Column(columnDefinition = "TEXT")
    private String namespace;

    /**
     * Identify when the sbom file was originally created.
     * Format: YYYY-MM-DDThh:mm:ssZ
     */
    @Column(columnDefinition = "TEXT")
    private String created;

    /**
     * The version of SPDX license list (<a href="https://spdx.dev/licenses/">...</a>) used in the related sbom document.
     * Data Format: "M.N"
     */
    @Column(columnDefinition = "TEXT", name = "license_list_version")
    private String licenseListVersion;

    // @OneToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_id_fk"))
    // private Product product;
    @Column(columnDefinition = "TEXT", name = "product_id")
    private String productId;

    /**
     * Packages referred in a sbom document.
     */
    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;

    /**
     * The list of subjects who created the related sbom document. At least one must be provided.
     */
    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SbomCreator> sbomCreators;

    /**
     * Element relationships in a sbom document.
     */
    @OneToMany(mappedBy = "sbom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SbomElementRelationship> sbomElementRelationships;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

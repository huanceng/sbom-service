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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

/**
 * Describes a package verification code.
 */
@Entity
@Table(indexes = {
        @Index(name = "pkg_id_uk", columnList = "pkg_id", unique = true)
})
public class PkgVerfCode {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Package verification code value in lower case hexadecimal representation.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    /**
     * Package that the verification code belongs to.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pkg_id", foreignKey = @ForeignKey(name = "pkg_id_fk"))
    private Package pkg;

    /**
     * The list of file excluded from the package verification code calculation.
     */
    @OneToMany(mappedBy = "pkgVerfCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public List<PkgVerfCodeExcludedFile> getPkgVerfCodeExcludedFiles() {
        return pkgVerfCodeExcludedFiles;
    }

    public void setPkgVerfCodeExcludedFiles(List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles) {
        this.pkgVerfCodeExcludedFiles = pkgVerfCodeExcludedFiles;
    }
}

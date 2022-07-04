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
 * Files excluded from the calculation of a package verification code.
 */
@Entity
@Table(indexes = {
        @Index(name = "pkg_verf_code_file_uk", columnList = "pkg_verf_code_id, file", unique = true)
})
public class PkgVerfCodeExcludedFile {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * File excluded from the package verification code calculation.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String file;

    /**
     * Package verification code that the excluded files belong to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pkg_verf_code_id", foreignKey = @ForeignKey(name = "pkg_verf_code_id_fk"))
    @JsonIgnore
    private PkgVerfCode pkgVerfCode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public PkgVerfCode getPkgVerfCode() {
        return pkgVerfCode;
    }

    public void setPkgVerfCode(PkgVerfCode pkgVerfCode) {
        this.pkgVerfCode = pkgVerfCode;
    }
}

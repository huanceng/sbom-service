package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SpdxPackage {
    @JsonProperty("SPDXID")
    private String spdxId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SpdxAnnotation> annotations;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> attributionTexts;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SpdxChecksum> checksums;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String comment;

    private String copyrightText;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    private String downloadLocation;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SpdxExternalReference> externalRefs;

    private Boolean filesAnalyzed;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> hasFiles;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String homepage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String licenseComments;

    private String licenseConcluded;

    private String licenseDeclared;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> licenseInfoFromFiles;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String originator;

    @JsonProperty("packageFileName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String packageFilename;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpdxPackageVerificationCode packageVerificationCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String sourceInfo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String summary;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String supplier;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String versionInfo;

    public SpdxPackage(@JsonProperty("SPDXID") String spdxId) {
        this.spdxId = spdxId;
    }

    @JsonCreator
    public SpdxPackage(
            @JsonProperty("SPDXID") String spdxId,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxAnnotation> annotations,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> attributionTexts,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxChecksum> checksums,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
            String copyrightText,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String description,
            String downloadLocation,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExternalReference> externalRefs,
            Boolean filesAnalyzed,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> hasFiles,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String homepage,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String licenseComments,
            String licenseConcluded,
            String licenseDeclared,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> licenseInfoFromFiles,
            String name,
            @JsonInclude(JsonInclude.Include.NON_NULL) String originator,
            @JsonProperty("packageFileName") @JsonInclude(JsonInclude.Include.NON_EMPTY) String packageFilename,
            @JsonInclude(JsonInclude.Include.NON_NULL) SpdxPackageVerificationCode packageVerificationCode,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String sourceInfo,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String summary,
            @JsonInclude(JsonInclude.Include.NON_NULL) String supplier,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) String versionInfo
    ) {
        this.spdxId = spdxId;
        this.annotations = annotations;
        this.attributionTexts = attributionTexts;
        this.checksums = checksums;
        this.comment = comment;
        this.copyrightText = copyrightText;
        this.description = description;
        this.downloadLocation = downloadLocation;
        this.externalRefs = externalRefs;
        this.filesAnalyzed = filesAnalyzed;
        this.hasFiles = hasFiles;
        this.homepage = homepage;
        this.licenseComments = licenseComments;
        this.licenseConcluded = licenseConcluded;
        this.licenseDeclared = licenseDeclared;
        this.licenseInfoFromFiles = licenseInfoFromFiles;
        this.name = name;
        this.originator = originator;
        this.packageFilename = packageFilename;
        this.packageVerificationCode = packageVerificationCode;
        this.sourceInfo = sourceInfo;
        this.summary = summary;
        this.supplier = supplier;
        this.versionInfo = versionInfo;
    }

    public String getSpdxId() {
        return spdxId;
    }

    public void setSpdxId(String spdxId) {
        this.spdxId = spdxId;
    }

    public List<SpdxAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<SpdxAnnotation> annotations) {
        this.annotations = annotations;
    }

    public List<String> getAttributionTexts() {
        return attributionTexts;
    }

    public void setAttributionTexts(List<String> attributionTexts) {
        this.attributionTexts = attributionTexts;
    }

    public List<SpdxChecksum> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<SpdxChecksum> checksums) {
        this.checksums = checksums;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public List<SpdxExternalReference> getExternalRefs() {
        return externalRefs;
    }

    public void setExternalRefs(List<SpdxExternalReference> externalRefs) {
        this.externalRefs = externalRefs;
    }

    public Boolean getFilesAnalyzed() {
        return filesAnalyzed;
    }

    public void setFilesAnalyzed(Boolean filesAnalyzed) {
        this.filesAnalyzed = filesAnalyzed;
    }

    public List<String> getHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(List<String> hasFiles) {
        this.hasFiles = hasFiles;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLicenseComments() {
        return licenseComments;
    }

    public void setLicenseComments(String licenseComments) {
        this.licenseComments = licenseComments;
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

    public List<String> getLicenseInfoFromFiles() {
        return licenseInfoFromFiles;
    }

    public void setLicenseInfoFromFiles(List<String> licenseInfoFromFiles) {
        this.licenseInfoFromFiles = licenseInfoFromFiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getPackageFilename() {
        return packageFilename;
    }

    public void setPackageFilename(String packageFilename) {
        this.packageFilename = packageFilename;
    }

    public SpdxPackageVerificationCode getPackageVerificationCode() {
        return packageVerificationCode;
    }

    public void setPackageVerificationCode(SpdxPackageVerificationCode packageVerificationCode) {
        this.packageVerificationCode = packageVerificationCode;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }
}

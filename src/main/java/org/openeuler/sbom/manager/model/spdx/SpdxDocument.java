package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;


public class SpdxDocument implements Serializable {

    public SpdxDocument(String spdxId) {
        this.spdxId = spdxId;
    }

    public SpdxDocument(@JsonProperty("SPDXID") String spdxId, String spdxVersion, SpdxCreationInfo creationInfo, String name, String dataLicense, @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExternalDocumentReference> externalDocumentRefs, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExtractedLicenseInfo> hasExtractedLicensingInfos, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxAnnotation> annotations, String documentNamespace, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> documentDescribes, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxPackage> packages, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxFile> files, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxSnippet> snippets, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxRelationship> relationships) {
        this.spdxId = spdxId;
        this.spdxVersion = spdxVersion;
        this.creationInfo = creationInfo;
        this.name = name;
        this.dataLicense = dataLicense;
        this.comment = comment;
        this.externalDocumentRefs = externalDocumentRefs;
        this.hasExtractedLicensingInfos = hasExtractedLicensingInfos;
        this.annotations = annotations;
        this.documentNamespace = documentNamespace;
        this.documentDescribes = documentDescribes;
        this.packages = packages;
        this.files = files;
        this.snippets = snippets;
        this.relationships = relationships;
    }

    @JsonProperty("SPDXID")
    String spdxId;

    String spdxVersion;

    SpdxCreationInfo creationInfo;

    String name;

    String dataLicense;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String comment;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxExternalDocumentReference> externalDocumentRefs;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxExtractedLicenseInfo> hasExtractedLicensingInfos;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxAnnotation> annotations;
    String documentNamespace;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> documentDescribes;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxPackage> packages;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxFile> files;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxSnippet> snippets;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SpdxRelationship> relationships;

    public String getSpdxId() {
        return spdxId;
    }

    public void setSpdxId(String spdxId) {
        this.spdxId = spdxId;
    }

    public String getSpdxVersion() {
        return spdxVersion;
    }

    public void setSpdxVersion(String spdxVersion) {
        this.spdxVersion = spdxVersion;
    }

    public SpdxCreationInfo getCreationInfo() {
        return creationInfo;
    }

    public void setCreationInfo(SpdxCreationInfo creationInfo) {
        this.creationInfo = creationInfo;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SpdxExternalDocumentReference> getExternalDocumentRefs() {
        return externalDocumentRefs;
    }

    public void setExternalDocumentRefs(List<SpdxExternalDocumentReference> externalDocumentRefs) {
        this.externalDocumentRefs = externalDocumentRefs;
    }

    public List<SpdxExtractedLicenseInfo> getHasExtractedLicensingInfos() {
        return hasExtractedLicensingInfos;
    }

    public void setHasExtractedLicensingInfos(List<SpdxExtractedLicenseInfo> hasExtractedLicensingInfos) {
        this.hasExtractedLicensingInfos = hasExtractedLicensingInfos;
    }

    public List<SpdxAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<SpdxAnnotation> annotations) {
        this.annotations = annotations;
    }

    public String getDocumentNamespace() {
        return documentNamespace;
    }

    public void setDocumentNamespace(String documentNamespace) {
        this.documentNamespace = documentNamespace;
    }

    public List<String> getDocumentDescribes() {
        return documentDescribes;
    }

    public void setDocumentDescribes(List<String> documentDescribes) {
        this.documentDescribes = documentDescribes;
    }

    public List<SpdxPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<SpdxPackage> packages) {
        this.packages = packages;
    }

    public List<SpdxFile> getFiles() {
        return files;
    }

    public void setFiles(List<SpdxFile> files) {
        this.files = files;
    }

    public List<SpdxSnippet> getSnippets() {
        return snippets;
    }

    public void setSnippets(List<SpdxSnippet> snippets) {
        this.snippets = snippets;
    }

    public List<SpdxRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<SpdxRelationship> relationships) {
        this.relationships = relationships;
    }
}

package org.openeuler.sbom.manager.model.vo;

import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.openeuler.sbom.manager.utils.PurlUtil;

import java.io.Serializable;

public class PackagePurlVo implements Serializable {

    private String id;

    private String name;

    private String version;

    private String supplier;

    private String description;

    private String copyright;

    private String summary;

    private String homepage;

    private String spdxId;

    private String downloadLocation;

    private boolean filesAnalyzed;

    private String licenseConcluded;

    private String licenseDeclared;

    private String sourceInfo;

    private String sbomId;

    private String purl;

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getSbomId() {
        return sbomId;
    }

    public void setSbomId(String sbomId) {
        this.sbomId = sbomId;
    }

    public static PackagePurlVo fromExternalPurlRef(ExternalPurlRef externalPurlRef) {
        PackagePurlVo packagePurlVo = new PackagePurlVo();

        packagePurlVo.setId(externalPurlRef.getPkg().getId().toString());
        packagePurlVo.setName(externalPurlRef.getPkg().getName());
        packagePurlVo.setVersion(externalPurlRef.getPkg().getVersion());
        packagePurlVo.setSupplier(externalPurlRef.getPkg().getSupplier());
        packagePurlVo.setDescription(externalPurlRef.getPkg().getDescription());
        packagePurlVo.setCopyright(externalPurlRef.getPkg().getCopyright());
        packagePurlVo.setSummary(externalPurlRef.getPkg().getSummary());
        packagePurlVo.setHomepage(externalPurlRef.getPkg().getHomepage());
        packagePurlVo.setSpdxId(externalPurlRef.getPkg().getSpdxId());
        packagePurlVo.setDownloadLocation(externalPurlRef.getPkg().getDownloadLocation());
        packagePurlVo.setFilesAnalyzed(externalPurlRef.getPkg().isFilesAnalyzed());
        packagePurlVo.setLicenseConcluded(externalPurlRef.getPkg().getLicenseConcluded());
        packagePurlVo.setLicenseDeclared(externalPurlRef.getPkg().getLicenseDeclared());
        packagePurlVo.setSourceInfo(externalPurlRef.getPkg().getSourceInfo());
        packagePurlVo.setSbomId(externalPurlRef.getPkg().getSbom().getId().toString());
        packagePurlVo.setPurl(PurlUtil.PackageUrlVoToPackageURL(externalPurlRef.getPurl()).canonicalize());

        return packagePurlVo;
    }
}

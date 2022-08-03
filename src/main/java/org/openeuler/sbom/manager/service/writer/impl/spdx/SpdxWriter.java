package org.openeuler.sbom.manager.service.writer.impl.spdx;

import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Checksum;
import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.openeuler.sbom.manager.model.ExternalVulRef;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.PkgVerfCode;
import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.SbomCreator;
import org.openeuler.sbom.manager.model.SbomElementRelationship;
import org.openeuler.sbom.manager.model.spdx.Algorithm;
import org.openeuler.sbom.manager.model.spdx.ReferenceCategory;
import org.openeuler.sbom.manager.model.spdx.ReferenceType;
import org.openeuler.sbom.manager.model.spdx.RelationshipType;
import org.openeuler.sbom.manager.model.spdx.SpdxChecksum;
import org.openeuler.sbom.manager.model.spdx.SpdxCreationInfo;
import org.openeuler.sbom.manager.model.spdx.SpdxDocument;
import org.openeuler.sbom.manager.model.spdx.SpdxExternalReference;
import org.openeuler.sbom.manager.model.spdx.SpdxPackage;
import org.openeuler.sbom.manager.model.spdx.SpdxPackageVerificationCode;
import org.openeuler.sbom.manager.model.spdx.SpdxRelationship;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.openeuler.sbom.manager.utils.PurlUtil;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = SbomConstants.SPDX_NAME + SbomConstants.WRITER_NAME)
@Transactional(rollbackFor = Exception.class)
public class SpdxWriter implements SbomWriter {
    private static final String SPDX_VERSION = "SPDX-2.2";
    @Autowired
    private SbomRepository sbomRepository;

    @Override
    public byte[] write(String productId, SbomFormat format) throws IOException {
        Sbom sbom = sbomRepository.findByProductId(productId).orElseThrow(() -> new RuntimeException("can't find %s's sbom metadata".formatted(productId)));
        SpdxDocument document = new SpdxDocument(sbom.getId().toString());

        document.setSpdxVersion(SPDX_VERSION);
        setCreationInfo(sbom, document);
        document.setName(sbom.getName());
        document.setDataLicense(sbom.getDataLicense());
        document.setComment(null);
        document.setExternalDocumentRefs(null);
        document.setHasExtractedLicensingInfos(null);
        document.setAnnotations(null);
        document.setDocumentNamespace(sbom.getNamespace());
        document.setDocumentDescribes(null);
        document.setPackages(sbom.getPackages().stream().map(this::transformPackage).toList());
        document.setFiles(null);
        document.setSnippets(null);
        document.setRelationships(sbom.getSbomElementRelationships().stream().map(this::transformRelationship).toList());

        return SbomMapperUtil.writeAsBytes(document, format);
    }

    private void setCreationInfo(Sbom sbom, SpdxDocument document) {
        List<String> creators = sbom.getSbomCreators().stream().map(SbomCreator::getName).collect(Collectors.toList());
        SpdxCreationInfo creationInfo = new SpdxCreationInfo(null, Instant.parse(sbom.getCreated()), creators, sbom.getLicenseListVersion());
        document.setCreationInfo(creationInfo);
    }

    private SpdxPackage transformPackage(Package pkg) {
        SpdxPackage spdxPackage = new SpdxPackage(pkg.getSpdxId());

        spdxPackage.setAnnotations(null);
        spdxPackage.setAttributionTexts(null);
        spdxPackage.setChecksums(pkg.getChecksums().stream().map(this::transformChecksum).toList());
        spdxPackage.setComment(null);
        spdxPackage.setCopyrightText(pkg.getCopyright());
        spdxPackage.setDescription(pkg.getDescription());
        spdxPackage.setDownloadLocation(pkg.getDownloadLocation());
        setExternalRefs(pkg, spdxPackage);
        spdxPackage.setFilesAnalyzed(pkg.isFilesAnalyzed());
        spdxPackage.setHasFiles(null);
        spdxPackage.setHomepage(pkg.getHomepage());
        spdxPackage.setLicenseComments(null);
        spdxPackage.setLicenseConcluded(pkg.getLicenseConcluded());
        spdxPackage.setLicenseDeclared(pkg.getLicenseDeclared());
        spdxPackage.setLicenseInfoFromFiles(null);
        spdxPackage.setName(pkg.getName());
        spdxPackage.setOriginator(null);
        spdxPackage.setPackageFilename(null);
        spdxPackage.setPackageVerificationCode(transformPkgVerfCode(pkg.getPkgVerfCode()));
        spdxPackage.setSourceInfo(pkg.getSourceInfo());
        spdxPackage.setSummary(pkg.getSummary());
        spdxPackage.setSupplier(pkg.getSupplier());
        spdxPackage.setVersionInfo(pkg.getVersion());

        return spdxPackage;
    }

    private SpdxChecksum transformChecksum(Checksum checksum) {
        if (Objects.isNull(checksum)) {
            return null;
        }

        return new SpdxChecksum(Algorithm.valueOf(checksum.getAlgorithm()), checksum.getValue());
    }

    private void setExternalRefs(Package pkg, SpdxPackage spdxPackage) {
        List<SpdxExternalReference> spdxExternalReferences = new ArrayList<>(
                pkg.getExternalPurlRefs().stream().map(this::transformExternalPurlRef).toList());
        spdxExternalReferences.addAll(pkg.getExternalVulRefs().stream().map(this::transformExternalVulRef).toList());
        spdxPackage.setExternalRefs(spdxExternalReferences);
    }

    private SpdxExternalReference transformExternalPurlRef(ExternalPurlRef ref) {
        if (Objects.isNull(ref)) {
            return null;
        }

        return new SpdxExternalReference(ref.getComment(), ReferenceCategory.valueOf(ref.getCategory()),
                ReferenceType.findReferenceType(ref.getType()), PurlUtil.PackageUrlVoToPackageURL(ref.getPurl()).canonicalize());
    }

    private SpdxExternalReference transformExternalVulRef(ExternalVulRef ref) {
        if (Objects.isNull(ref)) {
            return null;
        }

        return new SpdxExternalReference(ref.getComment(), ReferenceCategory.valueOf(ref.getCategory()),
                ReferenceType.findReferenceType(ref.getType()), ref.getVulnerability().getVulId());
    }

    private SpdxPackageVerificationCode transformPkgVerfCode(PkgVerfCode pkgVerfCode) {
        if (Objects.isNull(pkgVerfCode)) {
            return null;
        }

        return new SpdxPackageVerificationCode(
                pkgVerfCode.getPkgVerfCodeExcludedFiles().stream().map(PkgVerfCodeExcludedFile::getFile).toList(),
                pkgVerfCode.getValue());
    }

    private SpdxRelationship transformRelationship(SbomElementRelationship relationship) {
        if (Objects.isNull(relationship)) {
            return null;
        }

        return new SpdxRelationship(relationship.getElementId(), RelationshipType.valueOf(relationship.getRelationshipType()),
                relationship.getRelatedElementId(), relationship.getComment());
    }
}

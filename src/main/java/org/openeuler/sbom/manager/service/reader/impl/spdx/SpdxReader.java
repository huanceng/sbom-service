package org.openeuler.sbom.manager.service.reader.impl.spdx;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.ChecksumRepository;
import org.openeuler.sbom.manager.dao.ExternalPurlRefRepository;
import org.openeuler.sbom.manager.dao.PackageRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeExcludedFileRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeRepository;
import org.openeuler.sbom.manager.dao.PurlQualifierRepository;
import org.openeuler.sbom.manager.dao.PurlRepository;
import org.openeuler.sbom.manager.dao.SbomCreatorRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Checksum;
import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.PkgVerfCode;
import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.openeuler.sbom.manager.model.Purl;
import org.openeuler.sbom.manager.model.PurlQualifier;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.SbomCreator;
import org.openeuler.sbom.manager.model.spdx.ReferenceType;
import org.openeuler.sbom.manager.model.spdx.SpdxDocument;
import org.openeuler.sbom.manager.model.spdx.SpdxPackage;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomMapperUtil;
import org.openeuler.sbom.manager.utils.SbomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToExt;

@Service(value = SbomConstants.SPDX_NAME + SbomConstants.READER_NAME)
@Transactional(rollbackFor = Exception.class)
public class SpdxReader implements SbomReader {

    @Autowired
    private SbomRepository sbomRepository;

    @Autowired
    private SbomCreatorRepository sbomCreatorRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PkgVerfCodeRepository pkgVerfCodeRepository;

    @Autowired
    private PkgVerfCodeExcludedFileRepository pkgVerfCodeExcludedFileRepository;

    @Autowired
    private ChecksumRepository checksumRepository;

    @Autowired
    private ExternalPurlRefRepository externalPurlRefRepository;

    @Autowired
    private PurlRepository purlRepository;

    @Autowired
    private PurlQualifierRepository purlQualifierRepository;

    @Override
    public void read(File file) throws IOException {
        SbomFormat format = fileToExt(file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileContent = fileInputStream.readAllBytes();
        fileInputStream.close();

        read(format, fileContent);
    }

    @Override
    public void read(SbomFormat format, byte[] fileContent) throws IOException {
        SpdxDocument document = SbomMapperUtil.readDocument(format, SbomSpecification.SPDX_2_2.getDocumentClass(), fileContent);
        Sbom sbom = persistSbom(document);
        persistSbomCreators(document, sbom);
        persistPackages(document, sbom);
    }

    private Sbom persistSbom(SpdxDocument document) {
        Sbom sbom = sbomRepository.findById(document.spdxId()).orElse(new Sbom());
        sbom.setId(document.spdxId());
        sbom.setCreated(document.creationInfo().created().toString());
        sbom.setDataLicense(document.dataLicense());
        sbom.setLicenseListVersion(document.creationInfo().licenseListVersion());
        sbom.setName(document.name());
        sbom.setNamespace(document.documentNamespace());
        sbom.setSpec(SbomConstants.SPDX_NAME);
        sbom.setSpecVersion(document.spdxVersion().substring(document.spdxVersion().lastIndexOf("-") + 1));
        return sbomRepository.save(sbom);
    }

    private void persistSbomCreators(SpdxDocument document, Sbom sbom) {
        if (Objects.isNull(document.creationInfo().creators())) {
            return;
        }

        List<SbomCreator> sbomCreators = new ArrayList<>();
        document.creationInfo().creators().forEach(it -> {
            SbomCreator sbomCreator = Optional
                    .ofNullable(sbomCreatorRepository.findBySbomIdAndName(sbom.getId(), it))
                    .orElse(new SbomCreator());
            sbomCreator.setName(it);
            sbomCreator.setSbom(sbom);
            sbomCreators.add(sbomCreator);
        });
        sbomCreatorRepository.saveAll(sbomCreators);
    }

    private void persistPackages(SpdxDocument document, Sbom sbom) {
        if (Objects.isNull(document.packages())) {
            return;
        }

        document.packages().forEach(it -> {
            Package pkg = Optional
                    .ofNullable(packageRepository.findBySbomIdAndSpdxIdAndNameAndVersion(
                            sbom.getId(), it.spdxId(), it.name(), it.versionInfo()))
                    .orElse(new Package());
            pkg.setSpdxId(it.spdxId());
            pkg.setName(it.name());
            pkg.setVersion(it.versionInfo());
            pkg.setCopyright(it.copyrightText());
            pkg.setDescription(it.description());
            pkg.setDownloadLocation(it.downloadLocation());
            pkg.setFilesAnalyzed(it.filesAnalyzed());
            pkg.setHomepage(it.homepage());
            pkg.setLicenseConcluded(it.licenseConcluded());
            pkg.setLicenseDeclared(it.licenseDeclared());
            pkg.setSourceInfo(it.sourceInfo());
            pkg.setSummary(it.summary());
            pkg.setSupplier(it.supplier());
            pkg.setSbom(sbom);
            Package newPkg = packageRepository.save(pkg);

            PkgVerfCode pkgVerfCode = persistPkgVerfCode(it, newPkg);
            persistPkgVerfCodeExcludedFiles(it, pkgVerfCode);
            persistChecksums(it, newPkg);
            persistExternalRefs(it, newPkg);
        });
    }

    private PkgVerfCode persistPkgVerfCode(SpdxPackage spdxPackage, Package pkg) {
        if (Objects.isNull(spdxPackage.packageVerificationCode())) {
            return null;
        }

        PkgVerfCode pkgVerfCode = Optional.ofNullable(pkgVerfCodeRepository.findByPkgId(pkg.getId())).orElse(new PkgVerfCode());
        pkgVerfCode.setPkg(pkg);
        pkgVerfCode.setValue(spdxPackage.packageVerificationCode().packageVerificationCodeValue());
        return pkgVerfCodeRepository.save(pkgVerfCode);
    }

    private void persistPkgVerfCodeExcludedFiles(SpdxPackage spdxPackage, PkgVerfCode pkgVerfCode) {
        if (Objects.isNull(spdxPackage.packageVerificationCode()) ||
                Objects.isNull(spdxPackage.packageVerificationCode().packageVerificationCodeExcludedFiles())) {
            return;
        }

        List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles = new ArrayList<>();
        spdxPackage.packageVerificationCode().packageVerificationCodeExcludedFiles().forEach(f -> {
            PkgVerfCodeExcludedFile pkgVerfCodeExcludedFile = Optional
                    .ofNullable(pkgVerfCodeExcludedFileRepository.findByPkgVerfCodeIdAndFile(pkgVerfCode.getId(), f))
                    .orElse(new PkgVerfCodeExcludedFile());
            pkgVerfCodeExcludedFile.setFile(f);
            pkgVerfCodeExcludedFile.setPkgVerfCode(pkgVerfCode);
            pkgVerfCodeExcludedFiles.add(pkgVerfCodeExcludedFile);

        });
        pkgVerfCodeExcludedFileRepository.saveAll(pkgVerfCodeExcludedFiles);
    }

    private void persistChecksums(SpdxPackage spdxPackage, Package pkg) {
        if (Objects.isNull(spdxPackage.checksums())) {
            return;
        }

        List<Checksum> checksums = new ArrayList<>();
        spdxPackage.checksums().forEach(it -> {
            Checksum checksum = Optional
                    .ofNullable(checksumRepository.findByPkgIdAndAlgorithmAndValue(pkg.getId(), it.algorithm().toString(), it.checksumValue()))
                    .orElse(new Checksum());
            checksum.setAlgorithm(it.algorithm().toString());
            checksum.setValue(it.checksumValue());
            checksum.setPkg(pkg);
            checksums.add(checksum);
        });
        checksumRepository.saveAll(checksums);
    }

    private void persistExternalRefs(SpdxPackage spdxPackage, Package pkg) {
        if (Objects.isNull(spdxPackage.externalRefs())) {
            return;
        }

        List<ExternalPurlRef> externalPurlRefs = new ArrayList<>();
        spdxPackage.externalRefs().forEach(it -> {
            if (it.referenceType() == ReferenceType.PURL) {
                Purl purl = persistPurl(it.referenceLocator());
                ExternalPurlRef externalPurlRef = Optional
                        .ofNullable(externalPurlRefRepository.findByPkgIdAndPurlId(pkg.getId(), purl.getId()))
                        .orElse(new ExternalPurlRef());
                externalPurlRef.setCategory(it.referenceCategory().name());
                externalPurlRef.setType(it.referenceType().getType());
                externalPurlRef.setComment(it.comment());
                externalPurlRef.setPurl(purl);
                externalPurlRef.setPkg(pkg);
                externalPurlRefs.add(externalPurlRef);
            }
        });
        externalPurlRefRepository.saveAll(externalPurlRefs);
    }

    private Purl persistPurl(String purlStr) {
        PackageURL packageURL;
        try {
            packageURL = new PackageURL(purlStr);
        } catch (MalformedPackageURLException e) {
            throw new RuntimeException(e);
        }

        Purl temp = new Purl();
        temp.setType(packageURL.getType());
        temp.setName(packageURL.getName());
        temp.setNamespace(packageURL.getNamespace());
        temp.setVersion(packageURL.getVersion());
        temp.setSubPath(packageURL.getSubpath());
        temp.setQualifier(generatePurlQualifier(packageURL.getQualifiers()));

        Purl purl = purlRepository.findOne(Example.of(temp)).orElseGet(() -> purlRepository.save(temp));
        persistPurlQualifiers(purl, packageURL.getQualifiers());
        return purl;
    }

    private String generatePurlQualifier(Map<String, String> qualifiers) {
        final StringBuilder qualifier = new StringBuilder();
        if (qualifiers != null && qualifiers.size() > 0) {
            qualifiers.entrySet().stream().forEachOrdered((entry) -> {
                qualifier.append(entry.getKey());
                qualifier.append("=");
                qualifier.append(percentEncode(entry.getValue()));
                qualifier.append("&");
            });
            qualifier.setLength(qualifier.length() - 1);
        }
        return qualifier.toString();
    }

    private String percentEncode(final String input) {
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.name())
                    .replace("+", "%20")
                    // "*" is a reserved character in RFC 3986.
                    .replace("*", "%2A")
                    // "~" is an unreserved character in RFC 3986.
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return input; // this should never occur
        }
    }

    private void persistPurlQualifiers(Purl purl, Map<String, String> qualifiers) {
        if (qualifiers != null && qualifiers.size() > 0) {
            List<PurlQualifier> purlQualifiers = new ArrayList<>();
            qualifiers.entrySet().stream().forEachOrdered((entry) -> {
                PurlQualifier purlQualifier = Optional
                        .ofNullable(purlQualifierRepository.findByPurlIdAndKeyAndValue(purl.getId(), entry.getKey(), entry.getValue()))
                        .orElse(new PurlQualifier());
                purlQualifier.setKey(entry.getKey());
                purlQualifier.setValue(entry.getValue());
                purlQualifier.setPurl(purl);
                purlQualifiers.add(purlQualifier);
            });
            purlQualifierRepository.saveAll(purlQualifiers);
        }
    }
}

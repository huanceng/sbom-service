package org.openeuler.sbom.manager.service.reader.impl.spdx;

import org.openeuler.sbom.manager.dao.PackageRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeExcludedFileRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeRepository;
import org.openeuler.sbom.manager.dao.SbomCreatorRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.PkgVerfCode;
import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.SbomCreator;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomMapperUtil;
import org.openeuler.sbom.manager.utils.SbomSpecification;
import org.ossreviewtoolkit.utils.spdx.model.SpdxDocument;
import org.ossreviewtoolkit.utils.spdx.model.SpdxPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToExt;
import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToSpec;

@Service
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

    @Override
    public void read(File file) throws IOException {
        SbomFormat format = fileToExt(file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileContent = fileInputStream.readAllBytes();
        fileInputStream.close();

        SbomSpecification specification = fileToSpec(format, fileContent);
        read(format, specification, fileContent);
    }

    @Override
    public void read(SbomFormat format, SbomSpecification specification, byte[] fileContent) throws IOException {
        SpdxDocument document = SbomMapperUtil.readDocument(format, specification, fileContent);
        Sbom sbom = persistSbom(document);
        persistSbomCreators(document, sbom);
        persistPackages(document, sbom);
    }

    private Sbom persistSbom(SpdxDocument document) {
        Sbom sbom = sbomRepository.findById(document.getSpdxId()).orElse(new Sbom());
        sbom.setId(document.getSpdxId());
        sbom.setCreated(document.getCreationInfo().getCreated().toString());
        sbom.setDataLicense(document.getDataLicense());
        sbom.setLicenseListVersion(document.getCreationInfo().getLicenseListVersion());
        sbom.setName(document.getName());
        sbom.setNamespace(document.getDocumentNamespace());
        sbom.setSpec("SPDX");
        sbom.setSpecVersion(document.getSpdxVersion().substring(document.getSpdxVersion().lastIndexOf("-") + 1));
        return sbomRepository.save(sbom);
    }

    private void persistSbomCreators(SpdxDocument document, Sbom sbom) {
        List<SbomCreator> sbomCreators = new ArrayList<>();
        document.getCreationInfo().getCreators().forEach(it -> {
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
        document.getPackages().forEach(it -> {
            Package pkg = Optional
                    .ofNullable(packageRepository.findBySbomIdAndSpdxIdAndNameAndVersion(
                            sbom.getId(), it.getSpdxId(), it.getName(), it.getVersionInfo()))
                    .orElse(new Package());
            pkg.setSpdxId(it.getSpdxId());
            pkg.setName(it.getName());
            pkg.setVersion(it.getVersionInfo());
            pkg.setCopyright(it.getCopyrightText());
            pkg.setDescription(it.getDescription());
            pkg.setDownloadLocation(it.getDownloadLocation());
            pkg.setFilesAnalyzed(it.getFilesAnalyzed());
            pkg.setHomepage(it.getHomepage());
            pkg.setLicenseConcluded(it.getLicenseConcluded());
            pkg.setLicenseDeclared(it.getLicenseDeclared());
            pkg.setSourceInfo(it.getSourceInfo());
            pkg.setSummary(it.getSummary());
            pkg.setSupplier(it.getSupplier());
            pkg.setSbom(sbom);
            Package newPkg = packageRepository.save(pkg);

            PkgVerfCode pkgVerfCode = persistPkgVerfCode(it, newPkg);
            persistPkgVerfCodeExcludedFiles(it, pkgVerfCode);
        });
    }

    private PkgVerfCode persistPkgVerfCode(SpdxPackage spdxPackage, Package pkg) {
        if (Objects.isNull(spdxPackage.getPackageVerificationCode())) {
            return null;
        }

        PkgVerfCode pkgVerfCode = Optional.ofNullable(pkgVerfCodeRepository.findByPkgId(pkg.getId())).orElse(new PkgVerfCode());
        pkgVerfCode.setPkg(pkg);
        pkgVerfCode.setValue(spdxPackage.getPackageVerificationCode().getPackageVerificationCodeValue());
        return pkgVerfCodeRepository.save(pkgVerfCode);
    }

    private void persistPkgVerfCodeExcludedFiles(SpdxPackage spdxPackage, PkgVerfCode pkgVerfCode) {
        if (Objects.isNull(spdxPackage.getPackageVerificationCode())) {
            return;
        }

        List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles = new ArrayList<>();
        spdxPackage.getPackageVerificationCode().getPackageVerificationCodeExcludedFiles().forEach(f -> {
            PkgVerfCodeExcludedFile pkgVerfCodeExcludedFile = Optional
                    .ofNullable(pkgVerfCodeExcludedFileRepository.findByPkgVerfCodeIdAndFile(pkgVerfCode.getId(), f))
                    .orElse(new PkgVerfCodeExcludedFile());
            pkgVerfCodeExcludedFile.setFile(f);
            pkgVerfCodeExcludedFile.setPkgVerfCode(pkgVerfCode);
            pkgVerfCodeExcludedFiles.add(pkgVerfCodeExcludedFile);

        });
        pkgVerfCodeExcludedFileRepository.saveAll(pkgVerfCodeExcludedFiles);
    }
}

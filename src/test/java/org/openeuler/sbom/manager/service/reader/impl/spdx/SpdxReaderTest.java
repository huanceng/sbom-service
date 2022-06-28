package org.openeuler.sbom.manager.service.reader.impl.spdx;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpdxReaderTest {

    private static final String SAMPLE_UPLOAD_FILE_NAME = "sample/sample-spdx.json";

    @Autowired
    private SpdxReader spdxReader;

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

//    @Test
//    @Order(1)
//    public void setup() {
//        cleanDb();
//    }

    @Test
    @Order(2)
    public void insertSbom() throws IOException {
        functionBody();
    }

    @Test
    @Order(3)
    public void updateSbom() throws IOException {
        functionBody();
    }

    private void cleanDb() {
        sbomRepository.deleteAll();
        sbomCreatorRepository.deleteAll();
        packageRepository.deleteAll();
        pkgVerfCodeRepository.deleteAll();
        pkgVerfCodeExcludedFileRepository.deleteAll();
    }

    private void functionBody() throws IOException {
        spdxReader.read(new ClassPathResource(SAMPLE_UPLOAD_FILE_NAME).getFile());

        List<Sbom> sboms = sbomRepository.findAll();
        assertThat(sboms.size()).isEqualTo(1);
        assertThat(sboms.get(0).getId()).isEqualTo("SPDXRef-DOCUMENT");

        List<SbomCreator> sbomCreators = sbomCreatorRepository.findAll();
        assertThat(sbomCreators.size()).isEqualTo(1);
        assertThat(sbomCreators.get(0).getSbom().getId()).isEqualTo("SPDXRef-DOCUMENT");
        assertThat(sbomCreators.get(0).getName()).isEqualTo("Tool: OSS Review Toolkit - e5b343ff71-dirty");

        List<Package> packages = packageRepository.findAll();
        assertThat(packages.size()).isEqualTo(78);
        packages.forEach(p -> assertThat(p.getSbom().getId()).isEqualTo(sboms.get(0).getId()));

        List<Package> specificPackages = packageRepository.findBySpdxId("SPDXRef-Package-PyPI-asttokens-2.0.5-vcs");
        assertThat(specificPackages.size()).isEqualTo(1);
        assertThat(specificPackages.get(0).getName()).isEqualTo("asttokens");

        List<PkgVerfCode> pkgVerfCodes = pkgVerfCodeRepository.findAll();
        assertThat(pkgVerfCodes.size()).isEqualTo(1);
        assertThat(pkgVerfCodes.get(0).getValue()).isEqualTo("8aba92182455b539af15d0524fe5baffd3d9248b");

        List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles = pkgVerfCodeExcludedFileRepository.findAll();
        assertThat(pkgVerfCodeExcludedFiles.size()).isEqualTo(2);
    }
}
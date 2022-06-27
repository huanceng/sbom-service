package org.openeuler.sbom.manager.service.reader.impl.spdx;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Sbom;
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

    @Test
    public void addRecord() throws IOException {
        sbomRepository.deleteAll();
        spdxReader.read(new ClassPathResource(SAMPLE_UPLOAD_FILE_NAME).getFile());
        List<Sbom> sboms = sbomRepository.findAll();
        assertThat(sboms.size()).isEqualTo(1);
        assertThat(sboms.get(0).getId()).isEqualTo("SPDXRef-DOCUMENT");
    }
}
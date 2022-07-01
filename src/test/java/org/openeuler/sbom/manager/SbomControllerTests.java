package org.openeuler.sbom.manager;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openeuler.sbom.manager.service.SbomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {SbomManagerApplication.class, SbomApplicationContextHolder.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SbomControllerTests {

    private static final String SAMPLE_UPLOAD_FILE_NAME = "sample/sample-spdx.json";

    private static final String SAMPLE_PRODUCT_NAME = "openEuler-22.03-LTS-x86_64-dvd";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SbomService sbomService;


    @Test
    @Order(1)
    public void uploadSbomFile() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(SAMPLE_UPLOAD_FILE_NAME);
        MockMultipartFile file = new MockMultipartFile("uploadFileName", SAMPLE_UPLOAD_FILE_NAME
                , "json", classPathResource.getInputStream());

        this.mockMvc
                .perform(multipart("/sbom/uploadSbomFile").file(file)
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string("Success"));
    }

    @Test
    @Order(2)
    public void downloadSbomFileFailedNoSbom() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", SAMPLE_PRODUCT_NAME + ".iso")
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("can not find")));
    }

    @Test
    @Order(2)
    public void downloadSbomFileFailedNoSpec() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.3")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("sbom file specification: spdx - 2.3 is not support"));
    }

    @Test
    @Order(2)
    public void downloadSbomFileSuccess() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spdxVersion").value("SPDX-2.2"));
    }

    @Test
    @Order(2)
    public void exportSbomFailedNoSbom() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", SAMPLE_PRODUCT_NAME + ".iso")
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("can`t find")));
    }

    @Test
    @Order(2)
    public void exportSbomFailedNoSpec() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.3")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("sbom file specification: spdx - 2.3 is not support"));
    }

    @Test
    @Order(2)
    public void exportSbomJsonSuccess() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=openEuler-22.03-LTS-x86_64-dvd-spdx-sbom.json"))
                .andExpect(jsonPath("$.spdxVersion").value("SPDX-2.2"));
    }

    @Test
    @Order(2)
    public void exportSbomYamlSuccess() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "yaml")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=openEuler-22.03-LTS-x86_64-dvd-spdx-sbom.yaml"))
                .andExpect(content().string(containsString("spdxVersion: \"SPDX-2.2\"")));
    }

    @Test
    @Order(2)
    public void exportSbomXmlSuccess() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "xml")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=openEuler-22.03-LTS-x86_64-dvd-spdx-sbom.xml"))
                .andExpect(content().string(containsString("<spdxVersion>SPDX-2.2</spdxVersion>")));
    }
}


package org.openeuler.sbom.manager.controller;


import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.SbomApplicationContextHolder;
import org.openeuler.sbom.manager.SbomManagerApplication;
import org.openeuler.sbom.manager.TestCommon;
import org.openeuler.sbom.manager.TestConstants;
import org.openeuler.sbom.manager.model.spdx.SpdxDocument;
import org.openeuler.sbom.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SbomManagerApplication.class, SbomApplicationContextHolder.class})
@AutoConfigureMockMvc
public class ExportControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void downloadSbomFileFailedNoSbom() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME + ".iso")
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
    public void downloadSbomFileFailedNoSpec() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
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
    public void downloadSbomFileSuccess() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbomFile")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
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
    public void exportSbomFailedNoSbom() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME + ".iso")
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("can't find")));
    }

    @Test
    public void exportSbomFailedNoSpec() throws Exception {
        this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
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
    public void exportSbomJsonSuccess() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "json")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=" + TestConstants.SAMPLE_PRODUCT_NAME + "-spdx-sbom.json"))
                .andExpect(jsonPath("$.spdxVersion").value("SPDX-2.2"))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        SpdxDocument spdxDocument = Mapper.jsonSbomMapper.readValue(content, SpdxDocument.class);
        TestCommon.assertSpdxDocument(spdxDocument);
    }

    @Test
    public void exportSbomYamlSuccess() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "yaml")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=" + TestConstants.SAMPLE_PRODUCT_NAME + "-spdx-sbom.yaml"))
                .andExpect(content().string(containsString("spdxVersion: \"SPDX-2.2\"")))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        SpdxDocument spdxDocument = Mapper.yamlSbomMapper.readValue(content, SpdxDocument.class);
        TestCommon.assertSpdxDocument(spdxDocument);
    }

    @Test
    public void exportSbomXmlSuccess() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/sbom/exportSbom")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
                        .param("spec", "spdx")
                        .param("specVersion", "2.2")
                        .param("format", "xml")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=" + TestConstants.SAMPLE_PRODUCT_NAME + "-spdx-sbom.xml"))
                .andExpect(content().string(containsString("<spdxVersion>SPDX-2.2</spdxVersion>")))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        SpdxDocument spdxDocument = Mapper.xmlSbomMapper.readValue(content, SpdxDocument.class);
        TestCommon.assertSpdxDocument(spdxDocument);
    }
}


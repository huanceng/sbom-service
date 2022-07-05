package org.openeuler.sbom.manager.controller;


import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.SbomApplicationContextHolder;
import org.openeuler.sbom.manager.SbomManagerApplication;
import org.openeuler.sbom.manager.TestConstants;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.spdx.ReferenceCategory;
import org.openeuler.sbom.manager.service.SbomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SbomManagerApplication.class, SbomApplicationContextHolder.class})
@AutoConfigureMockMvc
public class PkgQueryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SbomService sbomService;

    @Test
    public void queryPackagesListByPageable() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackages")
                        .param("productId", TestConstants.SAMPLE_PRODUCT_NAME)
                        .param("page", "1")
                        .param("size", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.last").value(false))
                .andExpect(jsonPath("$.totalElements").value(78))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.content.[0].name").value("eigen"))
                .andExpect(jsonPath("$.content.[1].homepage").value("http://google.github.io/flatbuffers/"));
    }

    @Test
    public void queryPackagesListByName() throws Exception {
        this.mockMvc
                .perform(get("/sbom/querySbomPackages/%s/%s/%s".formatted(TestConstants.SAMPLE_PRODUCT_NAME, "pill", "false"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].spdxId").value("SPDXRef-Package-PyPI-pillow-9.1.1"))
                .andExpect(jsonPath("$.[1].spdxId").value("SPDXRef-Package-PyPI-pillow-9.1.1-source-artifact"))
                .andExpect(jsonPath("$.[0].name").value("pillow"))
                .andExpect(jsonPath("$.[1].name").value("pillow"));
    }

    private static String packageId = null;

    private void getPackageId() {
        if (PkgQueryControllerTests.packageId != null) {
            return;
        }

        List<Package> packagesList = sbomService.queryPackageInfoByName(TestConstants.OPENEULER_PRODUCT_NAME, TestConstants.BINARY_TEST_PACKAGE_NAME, true);
        assertThat(packagesList).isNotEmpty();

        PkgQueryControllerTests.packageId = packagesList.get(0).getId().toString();
    }

    @Test
    public void queryAllCategoryRef() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/queryPackageBinaryManagement/%s/%s".formatted(PkgQueryControllerTests.packageId, "all"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.packageList.*", hasSize(1)))
                .andExpect(jsonPath("$.provideList.*", hasSize(36)))
                .andExpect(jsonPath("$.externalList.*", hasSize(217)));
    }

    @Test
    public void queryPackageCategoryRef() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/queryPackageBinaryManagement/%s/%s".formatted(PkgQueryControllerTests.packageId, ReferenceCategory.PACKAGE_MANAGER.name()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.packageList.*", hasSize(1)))
                .andExpect(jsonPath("$.provideList.*", hasSize(0)))
                .andExpect(jsonPath("$.externalList.*", hasSize(0)));
    }

    @Test
    public void queryExternalCategoryRef() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/queryPackageBinaryManagement/%s/%s".formatted(PkgQueryControllerTests.packageId, ReferenceCategory.EXTERNAL_MANAGER.name()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.packageList.*", hasSize(0)))
                .andExpect(jsonPath("$.provideList.*", hasSize(0)))
                .andExpect(jsonPath("$.externalList.*", hasSize(217)));
    }

}


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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    public void queryPackagesListForPageable() throws Exception {
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
    public void queryPackagesListByExactlyNameForPageable() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackages")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("packageName", TestConstants.BINARY_TEST_PACKAGE_NAME)
                        .param("isExactly", Boolean.TRUE.toString())
                        .param("page", "0")
                        .param("size", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.content.[0].name").value("hive"))
                .andExpect(jsonPath("$.content.[0].homepage").value("http://hive.apache.org/"));
    }

    @Test
    public void queryPackagesListByFuzzyNameForPageable() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackages")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("packageName", TestConstants.BINARY_TEST_PACKAGE_NAME)
                        .param("isExactly", Boolean.FALSE.toString())
                        .param("page", "0")
                        .param("size", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.last").value(false))
                .andExpect(jsonPath("$.totalElements").value(28))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(15))
                .andExpect(jsonPath("$.content.[0].name").value("autoconf-archive"))
                .andExpect(jsonPath("$.content.[0].homepage").value("http://www.gnu.org/software/autoconf-archive/"))
                .andExpect(jsonPath("$.content.[2].name").value("hive"))
                .andExpect(jsonPath("$.content.[2].homepage").value("http://hive.apache.org/"));
    }

    @Test
    public void queryPackagesListByErrorNameForPageable() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackages")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("packageName", "hive-XXXX")
                        .param("isExactly", Boolean.FALSE.toString())
                        .param("page", "0")
                        .param("size", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.content.*", hasSize(0)));
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
    public void queryPackageByIdTest() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/querySbomPackage/%s".formatted(PkgQueryControllerTests.packageId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.name").value("hive"))
                .andExpect(jsonPath("$.version").value("0:3.1.2-3.oe2203"))
                .andExpect(jsonPath("$.homepage").value("http://hive.apache.org/"));
    }

    @Test
    public void queryPackageByErrorUUIDTest() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/querySbomPackage/%s".formatted("11"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().string("Invalid UUID string: 11"));
    }

    @Test
    public void queryPackageByErrorIdTest() throws Exception {
        if (PkgQueryControllerTests.packageId == null) {
            getPackageId();
        }
        this.mockMvc
                .perform(get("/sbom/querySbomPackage/%s".formatted("134aaa0c-1111-1111-1111-05686b9fc20c"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().string("packageId:134aaa0c-1111-1111-1111-05686b9fc20c is not exist"));
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

    @Test
    public void queryPackageInfoByBinaryExactlyTest() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackagesByBinary")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("binaryType", ReferenceCategory.EXTERNAL_MANAGER.name())
                        .param("type", "maven")
                        .param("namespace", "org.apache.zookeeper")
                        .param("name", "zookeeper")
                        .param("version", "3.4.6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].name").value("hive"));
    }

    @Test
    public void queryPackageInfoByBinaryWithoutVersionTest() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackagesByBinary")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("binaryType", ReferenceCategory.EXTERNAL_MANAGER.name())
                        .param("type", "maven")
                        .param("namespace", "org.apache.zookeeper")
                        .param("name", "zookeeper")
                        .param("version", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.*", hasSize(10)))
                .andExpect(jsonPath("$.[4].name").value("hive"));
    }

    @Test
    public void queryPackageInfoByBinaryOnlyNameTest() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackagesByBinary")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("binaryType", ReferenceCategory.EXTERNAL_MANAGER.name())
                        .param("type", "maven")
                        .param("namespace", "")
                        .param("name", "zookeeper")
                        .param("version", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.*", hasSize(12)))
                .andExpect(jsonPath("$.[6].name").value("hive"));
    }

    @Test
    public void queryPackageInfoByBinaryNoNameTest() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackagesByBinary")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("binaryType", ReferenceCategory.EXTERNAL_MANAGER.name())
                        .param("type", "maven")
                        .param("namespace", "zookeeper")
                        .param("name", "")
                        .param("version", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().string("maven purl query condition params is error, namespace: zookeeper, name: , version: "));
    }

    @Test
    public void queryPackageInfoByBinaryErrorTypeTest() throws Exception {
        this.mockMvc
                .perform(post("/sbom/querySbomPackagesByBinary")
                        .param("productId", TestConstants.OPENEULER_PRODUCT_NAME)
                        .param("binaryType", ReferenceCategory.EXTERNAL_MANAGER.name())
                        .param("type", "pip")
                        .param("namespace", "")
                        .param("name", "zookeeper")
                        .param("version", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().string("purl query condition not support type: pip"));
    }

}

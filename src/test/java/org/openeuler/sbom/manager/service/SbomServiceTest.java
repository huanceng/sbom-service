package org.openeuler.sbom.manager.service;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openeuler.sbom.manager.TestConstants;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.spdx.ReferenceCategory;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SbomServiceTest {


    @Autowired
    private SbomService sbomService;

    private static String packageId = null;

    private void getPackageId() {
        if (SbomServiceTest.packageId != null) {
            return;
        }

        List<Package> packagesList = sbomService.queryPackageInfoByName(TestConstants.OPENEULER_PRODUCT_NAME, TestConstants.BINARY_TEST_PACKAGE_NAME, true);
        assertThat(packagesList).isNotEmpty();

        SbomServiceTest.packageId = packagesList.get(0).getId().toString();
    }

    @Test
    @Order(2)
    public void getAllCategoryRef() {
        if (SbomServiceTest.packageId == null) {
            getPackageId();
        }
        BinaryManagementVo vo = sbomService.queryPackageBinaryManagement(SbomServiceTest.packageId, null);
        assertThat(vo.getPackageList().size()).isEqualTo(1);
        assertThat(vo.getProvideList().size()).isGreaterThan(1);
        assertThat(vo.getExternalList().size()).isGreaterThan(1);
    }

    @Test
    @Order(2)
    public void getPackageCategoryRef() {
        if (SbomServiceTest.packageId == null) {
            getPackageId();
        }
        BinaryManagementVo vo = sbomService.queryPackageBinaryManagement(SbomServiceTest.packageId, ReferenceCategory.PACKAGE_MANAGER.name());
        assertThat(vo.getPackageList().size()).isEqualTo(1);
        assertThat(vo.getProvideList().size()).isEqualTo(0);
        assertThat(vo.getExternalList().size()).isEqualTo(0);
    }

    @Test
    @Order(2)
    public void getProvideCategoryRef() {
        if (SbomServiceTest.packageId == null) {
            getPackageId();
        }
        BinaryManagementVo vo = sbomService.queryPackageBinaryManagement(SbomServiceTest.packageId, ReferenceCategory.PROVIDE_MANAGER.name());
        assertThat(vo.getPackageList().size()).isEqualTo(0);
        assertThat(vo.getProvideList().size()).isGreaterThan(1);
        assertThat(vo.getExternalList().size()).isEqualTo(0);
    }

    @Test
    @Order(2)
    public void getExternalCategoryRef() {
        if (SbomServiceTest.packageId == null) {
            getPackageId();
        }
        BinaryManagementVo vo = sbomService.queryPackageBinaryManagement(SbomServiceTest.packageId, ReferenceCategory.EXTERNAL_MANAGER.name());
        assertThat(vo.getPackageList().size()).isEqualTo(0);
        assertThat(vo.getProvideList().size()).isEqualTo(0);
        assertThat(vo.getExternalList().size()).isGreaterThan(1);
    }

}

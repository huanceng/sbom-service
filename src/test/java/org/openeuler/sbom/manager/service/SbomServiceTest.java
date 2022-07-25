package org.openeuler.sbom.manager.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.TestConstants;
import org.openeuler.sbom.manager.dao.ProductConfigRepository;
import org.openeuler.sbom.manager.dao.ProductRepository;
import org.openeuler.sbom.manager.dao.ProductTypeRepository;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.Product;
import org.openeuler.sbom.manager.model.ProductConfig;
import org.openeuler.sbom.manager.model.ProductType;
import org.openeuler.sbom.manager.model.spdx.ReferenceCategory;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.openeuler.sbom.manager.model.vo.PackagePurlVo;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.openeuler.sbom.manager.model.vo.PageVo;
import org.openeuler.sbom.manager.model.vo.ProductConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SbomServiceTest {

    @Autowired
    private SbomService sbomService;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductConfigRepository productConfigRepository;

    @Autowired
    private ProductRepository productRepository;

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
    public void getExternalCategoryRef() {
        if (SbomServiceTest.packageId == null) {
            getPackageId();
        }
        BinaryManagementVo vo = sbomService.queryPackageBinaryManagement(SbomServiceTest.packageId, ReferenceCategory.EXTERNAL_MANAGER.name());
        assertThat(vo.getPackageList().size()).isEqualTo(0);
        assertThat(vo.getProvideList().size()).isEqualTo(0);
        assertThat(vo.getExternalList().size()).isGreaterThan(1);
    }


    @Test
    public void queryPackageInfoByBinaryExactlyTest() throws Exception {
        PackageUrlVo purl=new PackageUrlVo("maven",
                "org.apache.zookeeper",
                "zookeeper",
                "3.4.6");
        Pageable pageable = PageRequest.of(0, 15).withSort(Sort.by(Sort.Order.by("name")));

        PageVo<PackagePurlVo> result = sbomService.queryPackageInfoByBinary(TestConstants.OPENEULER_PRODUCT_NAME,
                ReferenceCategory.EXTERNAL_MANAGER.name(),
                purl,
                pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void queryPackageInfoByBinaryWithoutVersionTest() throws Exception {
        PackageUrlVo purl=new PackageUrlVo("maven",
                "org.apache.zookeeper",
                "zookeeper",
                "");
        Pageable pageable = PageRequest.of(0, 15).withSort(Sort.by(Sort.Order.by("name")));

        PageVo<PackagePurlVo> result = sbomService.queryPackageInfoByBinary(TestConstants.OPENEULER_PRODUCT_NAME,
                ReferenceCategory.EXTERNAL_MANAGER.name(),
                purl,
                pageable);
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    public void queryPackageInfoByBinaryOnlyNameTest() throws Exception {
        PackageUrlVo purl=new PackageUrlVo("maven",
                "",
                "zookeeper",
                "");
        Pageable pageable = PageRequest.of(0, 15).withSort(Sort.by(Sort.Order.by("name")));

        PageVo<PackagePurlVo> result = sbomService.queryPackageInfoByBinary(TestConstants.OPENEULER_PRODUCT_NAME,
                ReferenceCategory.EXTERNAL_MANAGER.name(),
                purl,
                pageable);
        assertThat(result.getTotalElements()).isEqualTo(12);
    }

    @Test
    public void queryProductType() {
        ProductType productType = new ProductType();
        productType.setType("test_type_1");
        ProductType ret = productTypeRepository.save(productType);

        List<String> productTypes = sbomService.queryProductType();
        assertThat(productTypes.contains("test_type_1")).isTrue();

        productTypeRepository.delete(ret);
    }

    @Test
    public void queryProductConfigByProductType() {
        ProductType productType = new ProductType();
        productType.setType("test_type_2");

        ProductConfig config_1 = new ProductConfig();
        config_1.setProductType(productType);
        config_1.setValueType("String");
        config_1.setOrd(1);
        config_1.setName("os");

        ProductConfig config_2 = new ProductConfig();
        config_2.setProductType(productType);
        config_2.setValueType("String");
        config_2.setOrd(2);
        config_2.setName("arch");

        productType.setProductConfigs(List.of(config_1, config_2));
        ProductType ret = productTypeRepository.save(productType);

        List<ProductConfigVo> configVos = sbomService.queryProductConfigByProductType("test_type_2");
        assertThat(configVos.get(0).getName()).isEqualTo("os");
        assertThat(configVos.get(0).getValueType()).isEqualTo("String");
        assertThat(configVos.get(0).getOrd()).isEqualTo(1);
        assertThat(configVos.get(1).getName()).isEqualTo("arch");
        assertThat(configVos.get(1).getValueType()).isEqualTo("String");
        assertThat(configVos.get(1).getOrd()).isEqualTo(2);

        productTypeRepository.delete(ret);
    }

    @Test
    public void queryProductByFullAttributes() throws JsonProcessingException {
        Product product = new Product();
        product.setName("test_product");
        product.setAttribute(Map.of("os", "linux", "arch", "x86_64", "test", 1));
        Product ret = productRepository.save(product);

        Product product_found = sbomService.queryProductByFullAttributes(Map.of("os", "linux", "arch", "x86_64", "test", 1));
        assertThat(product_found.getName()).isEqualTo("test_product");
        assertThat(product_found.getAttribute().get("os")).isEqualTo("linux");
        assertThat(product_found.getAttribute().get("arch")).isEqualTo("x86_64");
        assertThat(product_found.getAttribute().get("test")).isEqualTo(1);

        productRepository.delete(ret);
    }
}

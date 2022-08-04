package org.openeuler.sbom.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.Product;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.openeuler.sbom.manager.model.vo.PackagePurlVo;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.openeuler.sbom.manager.model.vo.PageVo;
import org.openeuler.sbom.manager.model.vo.ProductConfigVo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SbomService {

    void readSbomFile(String productId, String fileName, byte[] fileContent) throws IOException;

    RawSbom writeSbomFile(String productId, String spec, String specVersion, String format);

    byte[] writeSbom(String productId, String spec, String specVersion, String format) throws IOException;

    PageVo<Package> findPackagesPageable(String productId, int page, int size);

    List<Package> queryPackageInfoByName(String productId, String packageName, boolean isExactly);

    Package queryPackageInfoById(String packageId);

    PageVo<Package> getPackageInfoByNameForPage(String productId, String packageName, Boolean isEqual, int page, int size);

    BinaryManagementVo queryPackageBinaryManagement(String packageId, String binaryType);

    @Deprecated
    PageVo<PackagePurlVo> queryPackageInfoByBinary(String productId,
                                                   String binaryType,
                                                   PackageUrlVo purl,
                                                   Pageable pageable) throws Exception;

    PageVo<PackagePurlVo> queryPackageInfoByBinaryViaSpec(String productId,
                                                          String binaryType,
                                                          PackageUrlVo purl,
                                                          Pageable pageable);

    List<String> queryProductType();

    List<ProductConfigVo> queryProductConfigByProductType(String productType);

    Product queryProductByFullAttributes(Map<String, ?> attributes) throws JsonProcessingException;

}

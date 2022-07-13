package org.openeuler.sbom.manager.service;

import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.vo.PackagePurlVo;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.openeuler.sbom.manager.model.vo.PageVo;

import java.io.IOException;
import java.util.List;

public interface SbomService {

    void readSbomFile(String productId, String fileName, byte[] fileContent) throws IOException;

    RawSbom writeSbomFile(String productId, String spec, String specVersion, String format);

    byte[] writeSbom(String productId, String spec, String specVersion, String format) throws IOException;

    PageVo<Package> findPackagesPageable(String productId, int page, int size);

    List<Package> queryPackageInfoByName(String productId, String packageName, boolean isExactly);

    PageVo<Package> getPackageInfoByNameForPage(String productId, String packageName, Boolean isEqual, int page, int size);

    BinaryManagementVo queryPackageBinaryManagement(String packageId, String binaryType);

    List<PackagePurlVo> queryPackageInfoByBinary(String productId,
                                                 String binaryType,
                                                 String type,
                                                 String namespace,
                                                 String name,
                                                 String version) throws Exception;
}

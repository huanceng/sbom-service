package org.openeuler.sbom.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.SbomApplicationContextHolder;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.ExternalPurlRefRepository;
import org.openeuler.sbom.manager.dao.PackageRepository;
import org.openeuler.sbom.manager.dao.ProductConfigRepository;
import org.openeuler.sbom.manager.dao.ProductRepository;
import org.openeuler.sbom.manager.dao.ProductTypeRepository;
import org.openeuler.sbom.manager.dao.RawSbomRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.Product;
import org.openeuler.sbom.manager.model.ProductConfig;
import org.openeuler.sbom.manager.model.ProductType;
import org.openeuler.sbom.manager.model.vo.PackagePurlVo;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.openeuler.sbom.manager.model.vo.PageVo;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.spdx.ReferenceCategory;
import org.openeuler.sbom.manager.model.vo.ProductConfigVo;
import org.openeuler.sbom.manager.service.SbomService;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.openeuler.sbom.manager.utils.EntityUtil;
import org.openeuler.sbom.manager.utils.PurlUtil;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomSpecification;
import org.openeuler.sbom.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToExt;
import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToSpec;

@Service
@Transactional(rollbackFor = Exception.class)
public class SbomServiceImpl implements SbomService {

    @Autowired
    private RawSbomRepository sbomFileRepository;

    @Autowired
    private SbomRepository sbomRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ExternalPurlRefRepository externalPurlRefRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductConfigRepository productConfigRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    // TODO 后续productID作为外键支持
    public void readSbomFile(String productId, String fileName, byte[] fileContent) throws IOException {
        SbomFormat format = fileToExt(fileName);
        SbomSpecification specification = fileToSpec(format, fileContent);

        RawSbom rawSbom = new RawSbom();
        rawSbom.setSpec(specification.getSpecification().toLowerCase());
        rawSbom.setSpecVersion(specification.getVersion());
        rawSbom.setFormat(format.getFileExtName());
        rawSbom.setProductId(productId);
        rawSbom.setValue(fileContent);

        RawSbom oldRawSbom = sbomFileRepository.queryRawSbom(rawSbom);
        if (oldRawSbom != null) {
            rawSbom.setId(oldRawSbom.getId());
            rawSbom.setCreateTime(oldRawSbom.getCreateTime());
        }
        sbomFileRepository.save(rawSbom);

        SbomReader sbomReader = SbomApplicationContextHolder.getSbomReader(specification.getSpecification());
        sbomReader.read(productId, format, fileContent);
    }

    @Override
    // TODO 后续productID作为外键支持
    public RawSbom writeSbomFile(String productId, String spec, String specVersion, String format) {
        format = StringUtils.lowerCase(format);
        spec = StringUtils.lowerCase(spec);

        if (!SbomFormat.EXT_TO_FORMAT.containsKey(format)) {
            throw new RuntimeException("sbom file format: %s is not support".formatted(format));
        }
        if (SbomSpecification.findSpecification(spec, specVersion) == null) {
            throw new RuntimeException("sbom file specification: %s - %s is not support".formatted(spec, specVersion));
        }

        RawSbom queryCondition = new RawSbom();
        queryCondition.setProductId(productId);
        queryCondition.setSpec(spec);
        queryCondition.setSpecVersion(specVersion);
        queryCondition.setFormat(format);

        return sbomFileRepository.queryRawSbom(queryCondition);
    }

    @Override
    public byte[] writeSbom(String productId, String spec, String specVersion, String format) throws IOException {
        format = StringUtils.lowerCase(format);
        spec = StringUtils.lowerCase(spec);

        if (!SbomFormat.EXT_TO_FORMAT.containsKey(format)) {
            throw new RuntimeException("sbom file format: %s is not support".formatted(format));
        }

        SbomSpecification sbomSpec = SbomSpecification.findSpecification(spec, specVersion);
        if (sbomSpec == null) {
            throw new RuntimeException("sbom file specification: %s - %s is not support".formatted(spec, specVersion));
        }

        SbomWriter sbomWriter = SbomApplicationContextHolder.getSbomWriter(sbomSpec.getSpecification());
        return sbomWriter.write(productId, SbomFormat.EXT_TO_FORMAT.get(format));
    }

    @Override
    public PageVo<Package> findPackagesPageable(String productId, int page, int size) {
        Sbom sbom = sbomRepository.findByProductId(productId).orElseThrow(() -> new RuntimeException("can't find %s `s sbom metadata".formatted(productId)));

        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Order.by("name")));
        return new PageVo<>((PageImpl<Package>) packageRepository.findPackagesBySbomIdForPage(sbom.getId(), pageable));
    }

    @Override
    public List<Package> queryPackageInfoByName(String productId, String packageName, boolean isExactly) {
        String equalPackageName = isExactly ? packageName : null;

        return packageRepository.getPackageInfoByName(productId, equalPackageName, packageName, SbomConstants.MAX_QUERY_LINE);
    }

    @Override
    public Package queryPackageInfoById(String packageId){
      return  packageRepository.findById(UUID.fromString(packageId)).orElse(null);
    }

    @Override
    public PageVo<Package> getPackageInfoByNameForPage(String productId, String packageName, Boolean isExactly, int page, int size) {
        String equalPackageName = BooleanUtils.isTrue(isExactly) ? packageName : null;
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Order.by("name")));

        return new PageVo<>((PageImpl<Package>) packageRepository.getPackageInfoByNameForPage(productId, isExactly, equalPackageName, packageName, pageable));
    }

    @Override
    public BinaryManagementVo queryPackageBinaryManagement(String packageId, String binaryType) {
        UUID packageUUID = UUID.fromString(packageId);
        ReferenceCategory referenceCategory = ReferenceCategory.findReferenceCategory(binaryType);
        if (referenceCategory != null && !ReferenceCategory.BINARY_TYPE.contains(referenceCategory)) {
            throw new RuntimeException("binary type: %s is not support".formatted(binaryType));
        }

        BinaryManagementVo vo = new BinaryManagementVo();
        if (referenceCategory == null || referenceCategory == ReferenceCategory.PACKAGE_MANAGER) {
            vo.setPackageList(externalPurlRefRepository.queryPackageRef(packageUUID, ReferenceCategory.PACKAGE_MANAGER.name()));
        }

        if (referenceCategory == null || referenceCategory == ReferenceCategory.PROVIDE_MANAGER) {
            vo.setProvideList(externalPurlRefRepository.queryPackageRef(packageUUID, ReferenceCategory.PROVIDE_MANAGER.name()));
        }

        if (referenceCategory == null || referenceCategory == ReferenceCategory.EXTERNAL_MANAGER) {
            vo.setExternalList(externalPurlRefRepository.queryPackageRef(packageUUID, ReferenceCategory.EXTERNAL_MANAGER.name()));
        }
        return vo;
    }

    @Override
    public PageVo<PackagePurlVo> queryPackageInfoByBinary(String productId,
                                                        String binaryType,
                                                        PackageUrlVo purl,
                                                        Pageable pageable
                                                        ) throws Exception {
        ReferenceCategory referenceCategory = ReferenceCategory.findReferenceCategory(binaryType);
        if (!ReferenceCategory.BINARY_TYPE.contains(referenceCategory)) {
            throw new RuntimeException("binary type: %s is not support".formatted(binaryType));
        }

        Pair<String, Boolean> purlQueryCondition = PurlUtil.generatePurlQueryCondition(purl);

        Page<Map> result = packageRepository.queryPackageInfoByBinary(productId,
                binaryType,
                purlQueryCondition.getSecond(),
                purlQueryCondition.getFirst(),
                purlQueryCondition.getFirst(),
                pageable);

        return new PageVo<>((PageImpl<PackagePurlVo>)EntityUtil.castEntity(result, PackagePurlVo.class));
    }

    @Override
    public List<String> queryProductType() {
        return productTypeRepository.findAll().stream().map(ProductType::getType).toList();
    }

    @Override
    public List<ProductConfigVo> queryProductConfigByProductType(String productType) {
        return productConfigRepository.findByProductTypeOrderByOrdAsc(productType)
                .stream()
                .map(it -> new ProductConfigVo(it.getName(), it.getValueType(), it.getOrd()))
                .toList();
    }

    public Product queryProductByFullAttributes(Map<String, ?> attributes) throws JsonProcessingException {
        String attr = Mapper.objectMapper.writeValueAsString(attributes);
        return productRepository.queryProductByFullAttributes(attr);
    }
}

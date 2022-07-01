package org.openeuler.sbom.manager.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.SbomApplicationContextHolder;
import org.openeuler.sbom.manager.dao.RawSbomRepository;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.service.SbomService;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToExt;
import static org.openeuler.sbom.manager.utils.SbomMapperUtil.fileToSpec;

@Service
@Transactional(rollbackFor = Exception.class)
public class SbomServiceImpl implements SbomService {

    @Autowired
    private RawSbomRepository sbomFileRepository;

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
}

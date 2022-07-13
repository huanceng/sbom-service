package org.openeuler.sbom.manager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ArrayUtils;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.model.vo.BinaryManagementVo;
import org.openeuler.sbom.manager.model.vo.PackagePurlVo;
import org.openeuler.sbom.manager.model.vo.PageVo;
import org.openeuler.sbom.manager.service.SbomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping(path = "/sbom")
public class SbomController {

    private static final Logger logger = LoggerFactory.getLogger(SbomController.class);

    @Autowired
    private SbomService sbomService;

    @PostMapping("/uploadSbomFile")
    public @ResponseBody ResponseEntity uploadSbomFile(HttpServletRequest request, @RequestParam String productId) throws IOException {//HttpServletRequest request
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("uploadFileName");
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("upload file is empty");
        }
        String fileName = file.getOriginalFilename();
        logger.info("upload {}`s sbom file name: {}, file length: {}", productId, fileName, file.getBytes().length);

        try {
            sbomService.readSbomFile(productId, fileName, file.getBytes());
        } catch (Exception e) {
            logger.error("uploadSbomFile failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
    }

    @RequestMapping("/exportSbomFile")
    public void exportSbomFile(HttpServletResponse response, @RequestParam String productId, @RequestParam String spec,
                               @RequestParam String specVersion, @RequestParam String format) throws IOException {
        logger.info("download original sbom file productId:{}, use spec:{}, specVersion:{}, format:{}",
                productId,
                spec,
                specVersion,
                format);
        RawSbom rawSbom = null;
        String errorMsg = null;

        try {
            rawSbom = sbomService.writeSbomFile(productId, spec, specVersion, format);
        } catch (Exception e) {
            logger.error("exportSbomFile failed", e);
            errorMsg = e.getMessage();
        }

        response.reset();

        if (rawSbom == null) {
            String returnContent =
                    StringUtils.hasText(errorMsg) ? errorMsg :
                            "can not find %s`s sbom, use spec:%s, specVersion:%s, format:%s".formatted(
                                    productId,
                                    spec,
                                    specVersion,
                                    format);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("text/plain");
            response.addHeader("Content-Length", "" + returnContent.getBytes(StandardCharsets.UTF_8).length);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(returnContent.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } else {
            byte[] exportContent = rawSbom.getValue();
            String fileName = "%s-%s-sbom.%s".formatted(productId, spec, format);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.addHeader("Content-Length", "" + exportContent.length);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(exportContent);
            outputStream.flush();
        }
    }

    @RequestMapping("/exportSbom")
    public void exportSbom(HttpServletResponse response, @RequestParam String productId, @RequestParam String spec,
                           @RequestParam String specVersion, @RequestParam String format) throws IOException {
        logger.info("download sbom metadata productId:{}, use spec:{}, specVersion:{}, format:{}",
                productId,
                spec,
                specVersion,
                format);
        byte[] sbom = null;
        String errorMsg = null;

        try {
            sbom = sbomService.writeSbom(productId, spec, specVersion, format);
        } catch (Exception e) {
            logger.error("export sbom metadata failed", e);
            errorMsg = e.getMessage();
        }

        response.reset();
        if (ArrayUtils.isEmpty(sbom)) {
            String returnContent =
                    StringUtils.hasText(errorMsg) ? errorMsg :
                            "can not find %s`s sbom metadata, use spec:%s, specVersion:%s, format:%s".formatted(
                                    productId,
                                    spec,
                                    specVersion,
                                    format);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("text/plain");
            response.addHeader("Content-Length", "" + returnContent.getBytes(StandardCharsets.UTF_8).length);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(returnContent.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } else {
            String fileName = "%s-%s-sbom.%s".formatted(productId, spec, format);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.addHeader("Content-Length", "" + sbom.length);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(sbom);
            outputStream.flush();
        }
    }

    @PostMapping("/querySbomPackages")
    public @ResponseBody ResponseEntity querySbomPackages(@RequestParam("productId") String productId,
                                                          @RequestParam(value = "packageName", required = false) String packageName,
                                                          @RequestParam(value = "isExactly", required = false) Boolean isExactly,
                                                          @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(name = "size", required = false, defaultValue = "15") Integer size) throws IOException {
        logger.info("query sbom packages by productId:{}, packageName:{}, isExactly:{}, page:{}, size:{}",
                productId,
                packageName,
                isExactly,
                page,
                size);
        PageVo<Package> packagesPage = sbomService.getPackageInfoByNameForPage(productId, packageName, isExactly, page, size);

        logger.info("query sbom packages result:{}", packagesPage);
        return ResponseEntity.status(HttpStatus.OK).body(packagesPage);
    }

    @GetMapping("/querySbomPackages/{productId}/{packageName}/{isExactly}")
    public @ResponseBody ResponseEntity getPackageInfoByName(@PathVariable("productId") String productId,
                                                             @PathVariable("packageName") String packageName,
                                                             @PathVariable(value = "isExactly") boolean isExactly) throws JsonProcessingException {
        logger.info("query sbom packages by productId:{}, packageName:{}, isExactly:{}", productId, packageName, isExactly);
        List<Package> packagesList = sbomService.queryPackageInfoByName(productId, packageName, isExactly);

        logger.info("query sbom packages result:{}", packagesList);
        return ResponseEntity.status(HttpStatus.OK).body(packagesList);
    }

    @GetMapping("/queryPackageBinaryManagement/{packageId}/{binaryType}")
    public @ResponseBody ResponseEntity queryPackageBinaryManagement(@PathVariable("packageId") String packageId,
                                                                     @PathVariable("binaryType") String binaryType) throws JsonProcessingException {
        logger.info("query package binary management by packageId:{}, binaryType:{}", packageId, binaryType);

        BinaryManagementVo result = sbomService.queryPackageBinaryManagement(packageId, binaryType);

        logger.info("query package binary management result:{}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PostMapping("/querySbomPackagesByBinary")
    public @ResponseBody ResponseEntity queryPackageInfoByBinary(@RequestParam("productId") String productId,
                                                                 @RequestParam("binaryType") String binaryType,
                                                                 @RequestParam("type") String type,
                                                                 @RequestParam(name = "namespace", required = false) String namespace,
                                                                 @RequestParam(name = "name", required = false) String name,
                                                                 @RequestParam(name = "version", required = false) String version) {
        logger.info("query package info by packageId:{}, binaryType:{}, type:{}, namespace:{}, name:{}, version:{}", productId,
                binaryType,
                type,
                namespace,
                name,
                version);


        List<PackagePurlVo> queryResult = null;
        try {
            queryResult = sbomService.queryPackageInfoByBinary(productId,
                    binaryType,
                    type,
                    namespace,
                    name,
                    version);
        } catch (RuntimeException e) {
            logger.error("query sbom packages failed.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            logger.error("query sbom packages failed.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("query sbom packages failed.");
        }

        logger.info("query sbom packages result:{}", queryResult == null ? 0 : queryResult.size());
        return ResponseEntity.status(HttpStatus.OK).body(queryResult);
    }
}

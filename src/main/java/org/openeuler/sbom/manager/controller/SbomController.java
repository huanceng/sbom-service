package org.openeuler.sbom.manager.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.openeuler.sbom.manager.model.RawSbom;
import org.openeuler.sbom.manager.service.SbomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

}

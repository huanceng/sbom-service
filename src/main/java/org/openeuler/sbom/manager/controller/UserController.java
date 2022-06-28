package org.openeuler.sbom.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.openeuler.sbom.manager.SbomManagerApplication;
import org.openeuler.sbom.manager.dao.UserRepository;
import org.openeuler.sbom.manager.model.PageVo;
import org.openeuler.sbom.manager.model.UserEntity;
import org.openeuler.sbom.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(SbomManagerApplication.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/addUser")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email) {
        userService.addNewUserByNameAndEmail(name, email);
        return "Saved";
    }

    @PostMapping(path = "/addUserRecord")
    public @ResponseBody ResponseEntity addNewUserEntity(@RequestBody UserEntity user) {
        userService.addNewUserByEntity(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Saved");
    }

    @GetMapping(path = "/allUser")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/uploadSbomFile")
    public @ResponseBody ResponseEntity uploadSingleFile(HttpServletRequest request, @RequestParam String artifactName) throws IOException {//HttpServletRequest request
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("upFileName");
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("upload file is empty");
        }
        String fileName = file.getOriginalFilename();

        StringWriter writer = new StringWriter();
        IOUtils.copy(file.getInputStream(), writer, StandardCharsets.UTF_8);
        String fileContent = writer.toString();

        logger.info("upload {}`s sbom file name: {}, file content: {}", artifactName, fileName, fileContent);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
    }

    @PostMapping("/uploadSbomFiles")
    public @ResponseBody ResponseEntity uploadMoreFile(@RequestParam("upFileNames") List<MultipartFile> files, @RequestParam String artifactName) throws IOException {//HttpServletRequest request
        if (Objects.isNull(files) || files.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("upload files is empty");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            StringWriter writer = new StringWriter();
            IOUtils.copy(file.getInputStream(), writer, StandardCharsets.UTF_8);
            String fileContent = writer.toString();
            logger.info("upload {}`s sbom file name: {}, file content: {}", artifactName, fileName, fileContent);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
    }


    @RequestMapping("/exportSbom")
    public void exportSbom(HttpServletResponse response, @RequestParam String artifactName) throws IOException {
        PageVo<UserEntity> pageResult = userService.findAllPageable("Third", 2, 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pageResult);
        logger.info("download sbom artifactName:{}", artifactName);

        byte[] exportContent = json.getBytes(StandardCharsets.UTF_8);

        String fileSuffix = artifactName.contains(".") ?
                artifactName.substring(0, artifactName.lastIndexOf(".")).toLowerCase() : artifactName;
        String fileName = fileSuffix + "-sbom.json";

        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.addHeader("Content-Length", "" + exportContent.length);

        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(exportContent);
        outputStream.flush();
    }

    @GetMapping("/downloadSbom/{artifactName}")
    public void downloadSbom(HttpServletResponse response, @PathVariable("artifactName") String artifactName, @RequestParam(required = false) String version) throws IOException {
        PageVo<UserEntity> pageResult = userService.findAllPageable("Third", 2, 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pageResult);
        logger.info("download sbom artifactName:{}, version:{}", artifactName, version);

        byte[] exportContent = json.getBytes(StandardCharsets.UTF_8);

        String fileSuffix = artifactName.contains(".") ?
                artifactName.substring(0, artifactName.lastIndexOf(".")).toLowerCase() : artifactName;
        String fileName = fileSuffix + "-sbom.json";

        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.addHeader("Content-Length", "" + exportContent.length);

        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(exportContent);
        outputStream.flush();
    }

}

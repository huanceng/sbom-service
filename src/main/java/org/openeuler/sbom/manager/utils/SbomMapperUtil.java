package org.openeuler.sbom.manager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.utils.Mapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SbomMapperUtil {

    @SuppressWarnings("unchecked")
    public static <T> T readDocument(SbomFormat format, SbomSpecification specification, byte[] fileContent) throws IOException {
        if (format == SbomFormat.JSON) {
            return fromJson(fileContent, (Class<T>) specification.getDocumentClass());
        }
        if (format == SbomFormat.XML) {
            return fromXml(fileContent, (Class<T>) specification.getDocumentClass());
        }
        if (format == SbomFormat.YAML) {
            return fromYaml(fileContent, (Class<T>) specification.getDocumentClass());
        }
        if (format == SbomFormat.RDF) {
            return fromRdf(fileContent, (Class<T>) specification.getDocumentClass());
        }
        throw new RuntimeException("invalid sbom file format %s".formatted(format.name()));
    }

    public static SbomFormat fileToExt(String fileName) {
        String fileExtStr = fileToExtStr(fileName);
        if (!SbomFormat.EXT_TO_FORMAT.containsKey(fileExtStr)) {
            throw new RuntimeException("invalid sbom file: %s".formatted(fileName));
        }

        return SbomFormat.EXT_TO_FORMAT.get(fileExtStr);
    }

    public static String fileToExtStr(String fileName) {
        if (!StringUtils.contains(fileName, ".")) {
            throw new RuntimeException("invalid sbom file without file extension: %s".formatted(fileName));
        }

        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (fileName.endsWith("rdf.xml")) {
            fileExt = "rdf.xml";
        }
        return fileExt;
    }

    public static SbomSpecification fileToSpec(SbomFormat format, byte[] fileContent) throws IOException {
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<>() {
        };
        HashMap<String, Object> map;
        if (format == SbomFormat.JSON) {
            map = Mapper.jsonSbomMapper.readValue(fileContent, typeReference);
        } else if (format == SbomFormat.XML) {
            map = Mapper.xmlSbomMapper.readValue(fileContent, typeReference);
        } else if (format == SbomFormat.YAML) {
            map = Mapper.yamlSbomMapper.readValue(fileContent, typeReference);
        } else if (format == SbomFormat.RDF) {
            throw new RuntimeException("not implemented for RDF");
        } else {
            throw new RuntimeException("invalid sbom file format type %s".formatted(format.name()));
        }

        if (StringUtils.equals((String) map.get("spdxVersion"), "SPDX-2.2")) {
            return SbomSpecification.SPDX_2_2;
        }
        if (StringUtils.equals((String) map.get("bomFormat"), "CycloneDX")) {
            if (StringUtils.equals((String) map.get("specVersion"), "1.4")) {
                return SbomSpecification.CYCLONEDX_1_4;
            }
        }
        throw new RuntimeException("failed to get sbom specification for sbom file %s");
    }

    private static <T> T fromJson(byte[] fileContent, Class<T> clazz) throws IOException {
        return Mapper.jsonSbomMapper.readValue(fileContent, clazz);
    }

    private static <T> T fromXml(byte[] fileContent, Class<T> clazz) throws IOException {
        return Mapper.xmlSbomMapper.readValue(fileContent, clazz);
    }

    private static <T> T fromYaml(byte[] fileContent, Class<T> clazz) throws IOException {
        return Mapper.yamlSbomMapper.readValue(fileContent, clazz);
    }

    private static <T> T fromRdf(byte[] fileContent, Class<T> clazz) throws IOException {
        throw new RuntimeException("not implemented for RDF");
    }

    public static <T> void write(T sbomDocument, File file, SbomFormat format) throws IOException {
        if (format == SbomFormat.JSON) {
            toJson(sbomDocument, file);
        } else if (format == SbomFormat.XML) {
            toXml(sbomDocument, file);
        } else if (format == SbomFormat.YAML) {
            toYaml(sbomDocument, file);
        } else if (format == SbomFormat.RDF) {
            toRdf(sbomDocument, file);
        } else {
            throw new RuntimeException("invalid format: %s".formatted(format));
        }
    }

    public static <T> String writeAsString(T sbomDocument, SbomFormat format) throws IOException {
        if (format == SbomFormat.JSON) {
            return toJsonString(sbomDocument);
        } else if (format == SbomFormat.XML) {
            return toXmlString(sbomDocument);
        } else if (format == SbomFormat.YAML) {
            return toYamlString(sbomDocument);
        } else if (format == SbomFormat.RDF) {
            return toRdfString(sbomDocument);
        } else {
            throw new RuntimeException("invalid format: %s".formatted(format));
        }
    }

    private static <T> void toJson(T sbomDocument, File file) throws IOException {
        Mapper.jsonSbomMapper.writerWithDefaultPrettyPrinter().writeValue(file, sbomDocument);
    }

    private static <T> void toXml(T sbomDocument, File file) throws IOException {
        Mapper.xmlSbomMapper.writerWithDefaultPrettyPrinter().writeValue(file, sbomDocument);
    }

    private static <T> void toYaml(T sbomDocument, File file) throws IOException {
        Mapper.yamlSbomMapper.writerWithDefaultPrettyPrinter().writeValue(file, sbomDocument);
    }

    private static <T> void toRdf(T sbomDocument, File file) throws IOException {
        throw new RuntimeException("not implemented for RDF");
    }

    private static <T> String toJsonString(T sbomDocument) throws JsonProcessingException {
        return Mapper.jsonSbomMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sbomDocument);
    }

    private static <T> String toXmlString(T sbomDocument) throws JsonProcessingException {
        return Mapper.xmlSbomMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sbomDocument);
    }

    private static <T> String toYamlString(T sbomDocument) throws JsonProcessingException {
        return Mapper.yamlSbomMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sbomDocument);
    }

    private static <T> String toRdfString(T sbomDocument) {
        throw new RuntimeException("not implemented for RDF");
    }
}

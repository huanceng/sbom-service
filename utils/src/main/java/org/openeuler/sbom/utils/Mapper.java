package org.openeuler.sbom.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mapper {
    public static ObjectMapper initMapper(ObjectMapper mapper) {
        return mapper.registerModule(new JavaTimeModule())
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static final JsonMapper jsonMapper = (JsonMapper) initMapper(new JsonMapper());

    public static final XmlMapper xmlMapper = (XmlMapper) initMapper(new XmlMapper());

    public static final YAMLMapper yamlMapper = (YAMLMapper) initMapper(new YAMLMapper());
}

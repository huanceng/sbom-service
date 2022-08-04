package org.openeuler.sbom.manager.model.vo;

import java.io.Serializable;
import java.util.TreeMap;

public class PackageUrlVo implements Serializable {

    public PackageUrlVo() {
    }

    public PackageUrlVo(String type, String namespace, String name, String version) {
        this.type = type;
        this.namespace = namespace;
        this.name = name;
        this.version = version;
    }

    public PackageUrlVo(String schema, String type, String namespace, String name, String version,
                        TreeMap<String, String> qualifiers, String subpath) {
        this.schema = schema;
        this.type = type;
        this.namespace = namespace;
        this.name = name;
        this.version = version;
        this.qualifiers = qualifiers;
        this.subpath = subpath;
    }

    private String schema;

    private String type;

    private String namespace;

    private String name;

    private String version;

    private TreeMap<String, String> qualifiers;

    private String subpath;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public TreeMap<String, String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(TreeMap<String, String> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public String getSubpath() {
        return subpath;
    }

    public void setSubpath(String subpath) {
        this.subpath = subpath;
    }
}

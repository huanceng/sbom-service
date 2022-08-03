package org.openeuler.sbom.manager.utils;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.springframework.data.util.Pair;

import java.util.TreeMap;

public class PurlUtil {
    public static PackageURL strToPackageURL(String purlStr) {
        try {
            return new PackageURL(purlStr);
        } catch (MalformedPackageURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String canonicalizePurl(String purl) {
        return strToPackageURL(purl).canonicalize();
    }

    public static PackageUrlVo strToPackageUrlVo(String purlStr) {
        PackageURL packageURL = strToPackageURL(purlStr);
        return new PackageUrlVo(packageURL.getScheme(), packageURL.getType(), packageURL.getNamespace(),
                packageURL.getName(), packageURL.getVersion(), (TreeMap<String, String>) packageURL.getQualifiers(),
                packageURL.getSubpath());
    }

    public static PackageURL PackageUrlVoToPackageURL(PackageUrlVo vo) {
        try {
            return new PackageURL(vo.getType(), vo.getNamespace(), vo.getName(), vo.getVersion(),
                    vo.getQualifiers(), vo.getSubpath());
        } catch (MalformedPackageURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pair<String, Boolean> generatePurlQueryCondition(PackageUrlVo purl) throws MalformedPackageURLException {
        String type = StringUtils.lowerCase(purl.getType());
        switch (type) {
            case "maven":
                return PurlUtil.generateMavenPurlQueryCondition(purl.getNamespace(), purl.getName(), purl.getVersion());
            case "rpm":
                return PurlUtil.generateRpmPurlQueryCondition(type, purl.getName(), purl.getVersion());
            case "pypi":
            case "github":
            case "gitlab":
            case "gitee":
                return PurlUtil.generateNoNamespacePurlQueryCondition(type, purl.getName(), purl.getVersion());
            // TODO 后续追加其他包管理的支持
        }

        throw new RuntimeException("purl query condition not support type: " + type);
    }

    private static Pair<String, Boolean> generateMavenPurlQueryCondition(String namespace, String name, String version) throws MalformedPackageURLException {
        String type = "maven";
        if (StringUtils.isNotEmpty(namespace) && StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(version)) {
            PackageURL purl = new PackageURL(type, namespace, name, version, null, null);
            return Pair.of(purl.getCoordinates(), true);
        } else if (StringUtils.isNotEmpty(namespace) && StringUtils.isNotEmpty(name)) {
            PackageURL purl = new PackageURL(type, namespace, name, null, null, null);
            return Pair.of(purl.getCoordinates(), false);
        } else if (StringUtils.isNotEmpty(name)) {
            return Pair.of(SbomConstants.PURL_SCHEMA_DEFAULT + ":" + type + "%" + name, false);
        } else {
            throw new RuntimeException("maven purl query condition params is error, namespace: %s, name: %s, version: %s".formatted(namespace, name, version));
        }
    }

    /**
     * RPM PURL使用name+version
     * <p>
     * 但是RPM的version比较特殊，使用epoch:version-release格式，转成PURL之后，PURL中的version为version-release；epoch放在PURL的qualifiers中，<a href="https://github.com/package-url/purl-spec/issues/69">https://github.com/package-url/purl-spec/issues/69</a>
     */
    private static Pair<String, Boolean> generateRpmPurlQueryCondition(String type, String name, String version) throws MalformedPackageURLException {
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(version)) {
            PackageURL purl = new PackageURL(type, null, name, version, null, null);
            return Pair.of(purl.getCoordinates(), false);
        } else if (StringUtils.isNotEmpty(name)) {
            return Pair.of(SbomConstants.PURL_SCHEMA_DEFAULT + ":" + type + "%" + name, false);
        } else {
            throw new RuntimeException("maven purl query condition params is error, type: %s, name: %s, version: %s".formatted(type, name, version));
        }
    }

    private static Pair<String, Boolean> generateNoNamespacePurlQueryCondition(String type, String name, String version) throws MalformedPackageURLException {
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(version)) {
            PackageURL purl = new PackageURL(type, null, name, version, null, null);
            return Pair.of(purl.getCoordinates(), true);
        } else if (StringUtils.isNotEmpty(name)) {
            return Pair.of(SbomConstants.PURL_SCHEMA_DEFAULT + ":" + type + "%" + name, false);
        } else {
            throw new RuntimeException("maven purl query condition params is error, type: %s, name: %s, version: %s".formatted(type, name, version));
        }
    }
}

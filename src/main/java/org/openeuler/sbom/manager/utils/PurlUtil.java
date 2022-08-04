package org.openeuler.sbom.manager.utils;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.Map;
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

    @Deprecated
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

    @Deprecated
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
    @Deprecated
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

    @Deprecated
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

    public static Map<String, Pair<String, Boolean>> generatePurlQueryConditionMap(PackageUrlVo purl) {
        String type = StringUtils.lowerCase(purl.getType());
        switch (type) {
            case "maven":
                return PurlUtil.generateMavenPurlQueryConditionMap(purl.getNamespace(), purl.getName(), purl.getVersion());
            case "rpm":
                return PurlUtil.generateRpmPurlQueryConditionMap(purl.getName(), purl.getVersion());
            case "pypi":
            case "github":
            case "gitlab":
            case "gitee":
                return PurlUtil.generateNoNamespacePurlQueryConditionMap(type, purl.getName(), purl.getVersion());
            // TODO 后续追加其他包管理的支持
        }

        throw new RuntimeException("purl query condition not support type: " + type);
    }

    /**
     * 根据Maven PURL参数拼装查询参数:
     * <p>
     * 1. name必须有值
     * <p>
     * 2. name仅在version和namespace均有值场景下才进行精确查询，否则使用模糊匹配
     * <p>
     * 3. version和namespace若有值进行精确查询
     */
    private static Map<String, Pair<String, Boolean>> generateMavenPurlQueryConditionMap(String namespace, String name, String version) {
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("maven purl query condition params is error, namespace: %s, name: %s, version: %s".formatted(namespace, name, version));
        }
        Map<String, Pair<String, Boolean>> purlComponents = CollectionUtils.newHashMap(0);
        purlComponents.put("type", Pair.of(SbomConstants.PURL_MAVEN_TYPE_VALUE, true));

        if (StringUtils.isNotEmpty(version)) {
            purlComponents.put("version", Pair.of(version, true));
        }
        if (StringUtils.isNotEmpty(namespace)) {
            purlComponents.put("namespace", Pair.of(namespace, true));
        }

        if (StringUtils.isNotEmpty(namespace) && StringUtils.isNotEmpty(version)) {
            purlComponents.put("name", Pair.of(name, true));
        } else {
            purlComponents.put("name", Pair.of(name, false));
        }

        return purlComponents;
    }

    /**
     * RPM的version比较特殊，使用epoch:version-release格式，转成PURL之后，PURL中的version为version-release；epoch放在PURL的qualifiers中，<a href="https://github.com/package-url/purl-spec/issues/69">https://github.com/package-url/purl-spec/issues/69</a>
     * <p>
     * 根据RPM PURL(name+version)参数拼装查询参数:
     * <p>
     * 1. name必须有值
     * <p>
     * 2. name在version有值场景下进行精确查询，否则使用模糊匹配
     * <p>
     * 3. version若有值进行模糊匹配(页面仅需要传值epoch:version-release中的version部分)
     */
    private static Map<String, Pair<String, Boolean>> generateRpmPurlQueryConditionMap(String name, String version) {
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("rpm purl query condition params is error, name: %s, version: %s".formatted(name, version));
        }
        Map<String, Pair<String, Boolean>> purlComponents = CollectionUtils.newHashMap(0);
        purlComponents.put("type", Pair.of(SbomConstants.PURL_RPM_TYPE_VALUE, true));

        if (StringUtils.isNotEmpty(version)) {
            purlComponents.put("version", Pair.of(version, false));
            purlComponents.put("name", Pair.of(name, true));
        } else {
            purlComponents.put("name", Pair.of(name, false));
        }

        return purlComponents;
    }

    /**
     * 根据PURL(name+version)参数拼装查询参数:
     * <p>
     * 1. name必须有值
     * <p>
     * 2. name在version有值场景下进行精确查询，否则使用模糊匹配
     * <p>
     * 3. version若有值进行精确查询
     */
    private static Map<String, Pair<String, Boolean>> generateNoNamespacePurlQueryConditionMap(String type, String name, String version) {
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("%s purl query condition params is error, name: %s, version: %s".formatted(type, name, version));
        }
        Map<String, Pair<String, Boolean>> purlComponents = CollectionUtils.newHashMap(0);
        purlComponents.put("type", Pair.of(type, true));

        if (StringUtils.isNotEmpty(version)) {
            purlComponents.put("version", Pair.of(version, true));
            purlComponents.put("name", Pair.of(name, true));
        } else {
            purlComponents.put("name", Pair.of(name, false));
        }

        return purlComponents;
    }

}

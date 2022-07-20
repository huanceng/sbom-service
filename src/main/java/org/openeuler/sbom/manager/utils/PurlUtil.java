package org.openeuler.sbom.manager.utils;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.springframework.data.util.Pair;

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

    public static Pair<String, Boolean> generatePurlQueryCondition(PackageUrlVo purl) throws MalformedPackageURLException {
        String type = StringUtils.lowerCase(purl.getType());
        switch (type) {
            case "maven":
                return PurlUtil.generateMavenPurlQueryCondition(purl.getNamespace(), purl.getName(), purl.getVersion());
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
            return Pair.of(name, false);
        } else {
            throw new RuntimeException("maven purl query condition params is error, namespace: %s, name: %s, version: %s".formatted(namespace, name, version));
        }
    }
}

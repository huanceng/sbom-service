package org.openeuler.sbom.manager.utils;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;

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
}

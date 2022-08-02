package org.openeuler.sbom.manager.service;

import com.github.packageurl.MalformedPackageURLException;
import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.openeuler.sbom.manager.utils.PurlUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PurlQueryConditionTest {

    @Test
    public void mavenOnlyNameTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("maven", "", "zookeeper", ""));

        assertThat(result.getFirst()).isEqualTo("pkg:maven%zookeeper");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void mavenNameAndNamespaceTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("maven", "org.apache.zookeeper", "zookeeper", ""));

        assertThat(result.getFirst()).isEqualTo("pkg:maven/org.apache.zookeeper/zookeeper");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void mavenGavTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("maven", "org.apache.zookeeper", "zookeeper", "3.4.6"));

        assertThat(result.getFirst()).isEqualTo("pkg:maven/org.apache.zookeeper/zookeeper@3.4.6");
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    public void notSupportTypeTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = null;
        try {
            result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("pip", "org.apache.zookeeper", "zookeeper", "3.4.6"));
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("purl query condition not support type: pip");
        }
        assertThat(result).isNull();
    }

    @Test
    public void pypiOnlyNameTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("pypi", "", "numpy", ""));

        assertThat(result.getFirst()).isEqualTo("pkg:pypi%numpy");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void pypiNameAndVersionTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("pypi", "", "numpy", "5.9.1"));

        assertThat(result.getFirst()).isEqualTo("pkg:pypi/numpy@5.9.1");
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    public void rpmOnlyNameTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("rpm", "", "openssl", ""));

        assertThat(result.getFirst()).isEqualTo("pkg:rpm%openssl");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void rpmNameAndVersionTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition(new PackageUrlVo("rpm", "", "openssl", "1.2.3"));

        assertThat(result.getFirst()).isEqualTo("pkg:rpm/openssl@1.2.3");
        assertThat(result.getSecond()).isFalse();
    }

}

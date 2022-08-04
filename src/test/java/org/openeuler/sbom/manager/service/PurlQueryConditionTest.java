package org.openeuler.sbom.manager.service;

import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.openeuler.sbom.manager.utils.PurlUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PurlQueryConditionTest {

    @Test
    public void mavenOnlyNameTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("maven", "", "zookeeper", ""));

        assertThat(result.get("type").getFirst()).isEqualTo("maven");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("zookeeper");
        assertThat(result.get("name").getSecond()).isFalse();

        assertThat(result.containsValue("namespace")).isFalse();
        assertThat(result.containsValue("version")).isFalse();
    }

    @Test
    public void mavenNameAndNamespaceTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("maven", "org.apache.zookeeper", "zookeeper", ""));

        assertThat(result.get("type").getFirst()).isEqualTo("maven");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("namespace").getFirst()).isEqualTo("org.apache.zookeeper");
        assertThat(result.get("namespace").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("zookeeper");
        assertThat(result.get("name").getSecond()).isFalse();

        assertThat(result.containsValue("version")).isFalse();
    }

    @Test
    public void mavenGavTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("maven", "org.apache.zookeeper", "zookeeper", "3.4.6"));

        assertThat(result.get("type").getFirst()).isEqualTo("maven");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("namespace").getFirst()).isEqualTo("org.apache.zookeeper");
        assertThat(result.get("namespace").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("zookeeper");
        assertThat(result.get("name").getSecond()).isTrue();

        assertThat(result.get("version").getFirst()).isEqualTo("3.4.6");
        assertThat(result.get("version").getSecond()).isTrue();
    }

    @Test
    public void notSupportTypeTest() {
        Map<String, Pair<String, Boolean>> result = null;
        try {
            result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("pip", "org.apache.zookeeper", "zookeeper", "3.4.6"));
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("purl query condition not support type: pip");
        }
        assertThat(result).isNull();
    }

    @Test
    public void pypiOnlyNameTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("pypi", "", "numpy", ""));

        assertThat(result.get("type").getFirst()).isEqualTo("pypi");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("numpy");
        assertThat(result.get("name").getSecond()).isFalse();

        assertThat(result.containsValue("version")).isFalse();
    }

    @Test
    public void pypiNameAndVersionTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("pypi", "", "numpy", "5.9.1"));

        assertThat(result.get("type").getFirst()).isEqualTo("pypi");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("numpy");
        assertThat(result.get("name").getSecond()).isTrue();

        assertThat(result.get("version").getFirst()).isEqualTo("5.9.1");
        assertThat(result.get("version").getSecond()).isTrue();
    }

    @Test
    public void rpmOnlyNameTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("rpm", "", "openssl", ""));

        assertThat(result.get("type").getFirst()).isEqualTo("rpm");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("openssl");
        assertThat(result.get("name").getSecond()).isFalse();

        assertThat(result.containsValue("version")).isFalse();
    }

    @Test
    public void rpmNameAndVersionTest() {
        Map<String, Pair<String, Boolean>> result = PurlUtil.generatePurlQueryConditionMap(new PackageUrlVo("rpm", "", "openssl", "1.2.3"));

        assertThat(result.get("type").getFirst()).isEqualTo("rpm");
        assertThat(result.get("type").getSecond()).isTrue();

        assertThat(result.get("name").getFirst()).isEqualTo("openssl");
        assertThat(result.get("name").getSecond()).isTrue();

        assertThat(result.get("version").getFirst()).isEqualTo("1.2.3");
        assertThat(result.get("version").getSecond()).isFalse();
    }

}

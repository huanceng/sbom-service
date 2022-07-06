package org.openeuler.sbom.manager.service;

import com.github.packageurl.MalformedPackageURLException;
import org.junit.jupiter.api.Test;
import org.openeuler.sbom.manager.utils.PurlUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PurlQueryConditionTest {

    @Test
    public void mavenOnlyNameTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition("maven", "", "zookeeper", "");

        assertThat(result.getFirst()).isEqualTo("zookeeper");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void mavenNameAndNamespaceTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition("maven", "org.apache.zookeeper", "zookeeper", "");

        assertThat(result.getFirst()).isEqualTo("pkg:maven/org.apache.zookeeper/zookeeper");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    public void mavenGavTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = PurlUtil.generatePurlQueryCondition("maven", "org.apache.zookeeper", "zookeeper", "3.4.6");

        assertThat(result.getFirst()).isEqualTo("pkg:maven/org.apache.zookeeper/zookeeper@3.4.6");
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    public void notSupportTypeTest() throws MalformedPackageURLException {
        Pair<String, Boolean> result = null;
        try {
            result = PurlUtil.generatePurlQueryCondition("pip", "org.apache.zookeeper", "zookeeper", "3.4.6");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("purl query condition not support type: pip");
        }
        assertThat(result).isNull();
    }

}

package org.openeuler.sbom.manager.dao.spec;

import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.vo.PackageUrlVo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ExternalPurlRefSpecs {

    private static final List<String> PURL_COMPONENTS =
            Arrays.stream(PackageUrlVo.class.getDeclaredFields()).map(Field::getName).toList();

    public static Specification<ExternalPurlRef> hasSbomId(UUID sbomId) {
        return (root, query, criteriaBuilder) -> {
            Join<Package, ExternalPurlRef> join = root.join("pkg");
            return criteriaBuilder.equal(join.get("sbom").get("id"), sbomId);
        };
    }

    public static Specification<ExternalPurlRef> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<ExternalPurlRef> hasPurlComponent(Map<String, Pair<String, Boolean>> purlComponents) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            purlComponents.forEach((key, value) -> {
                if (PURL_COMPONENTS.contains(key)) {
                    if (StringUtils.isNotEmpty(value.getFirst())) {
                        if (value.getSecond()) {
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.function("jsonb_extract_path_text",
                                    String.class, root.get("purl"), criteriaBuilder.literal(key)), value.getFirst()));
                        } else {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.function("jsonb_extract_path_text",
                                    String.class, root.get("purl"), criteriaBuilder.literal(key)),
                                    MessageFormat.format("%{0}%", value.getFirst())));
                        }
                    }
                } else {
                    if (StringUtils.isNotEmpty(value.getFirst())) {
                        if (value.getSecond()) {
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.function("jsonb_extract_path_text",
                                    String.class, root.get("purl"), criteriaBuilder.literal("qualifiers"),
                                    criteriaBuilder.literal(key)), value.getFirst()));
                        } else {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.function("jsonb_extract_path_text",
                                    String.class, root.get("purl"), criteriaBuilder.literal("qualifiers"),
                                    criteriaBuilder.literal(key)), MessageFormat.format("%{0}%", value.getFirst())));
                        }
                    }
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
    List<Package> findBySpdxId(String spdxId);

    @Query(value = "select * from package where sbom_id = :sbomId",
            //  两个count配置的效果等价
            countProjection = "1",
            // countQuery = "select count(1) from package where sbom_id = :sbomId",
            nativeQuery = true)
    Page<Package> findPackagesBySbomIdForPage(@Param("sbomId") UUID sbomId, Pageable pageable);

    @Query(value = "SELECT * FROM package WHERE sbom_id = ( SELECT id FROM sbom WHERE product_id = :productId) " +
            "AND (:equalPackageName IS NULL OR name = :equalPackageName) AND name LIKE %:likePackageName% limit :maxLine",
            nativeQuery = true)
    List<Package> getPackageInfoByName(@Param("productId") String productId,
                                       @Param("equalPackageName") String equalPackageName,
                                       @Param("likePackageName") String likePackageName,
                                       @Param("maxLine") Integer maxLine);


}
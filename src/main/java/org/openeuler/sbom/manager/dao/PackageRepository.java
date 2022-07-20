package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {

    List<Package> findBySbomId(UUID sbomId);

    List<Package> findBySbomIdAndSpdxId(UUID sbomId, String spdxId);

    List<Package> findBySpdxId(String spdxId);

    @Query(value = "select * from package where sbom_id = :sbomId",
            //  两个count配置的效果等价
            countProjection = "1",
            // countQuery = "select count(1) from package where sbom_id = :sbomId",
            nativeQuery = true)
    Page<Package> findPackagesBySbomIdForPage(@Param("sbomId") UUID sbomId, Pageable pageable);

    @Query(value = "SELECT * FROM package WHERE sbom_id = ( SELECT id FROM sbom WHERE product_id = :productId) " +
            "AND (:equalPackageName IS NULL OR name = :equalPackageName) AND (:likePackageName IS NULL OR (name LIKE %:likePackageName%)) limit :maxLine",
            nativeQuery = true)
    List<Package> getPackageInfoByName(@Param("productId") String productId,
                                       @Param("equalPackageName") String equalPackageName,
                                       @Param("likePackageName") String likePackageName,
                                       @Param("maxLine") Integer maxLine);

    @Query(value = "SELECT * FROM package WHERE sbom_id = ( SELECT id FROM sbom WHERE product_id = :productId) " +
            "AND (:isExactly IS NULL OR :isExactly = FALSE OR (name = :equalPackageName)) " +
            "AND (:isExactly IS NULL OR :isExactly = TRUE OR (name LIKE %:likePackageName%))",
            countProjection = "1",
            nativeQuery = true)
    Page<Package> getPackageInfoByNameForPage(@Param("productId") String productId,
                                              @Param("isExactly") Boolean isExactly,
                                              @Param("equalPackageName") String equalPackageName,
                                              @Param("likePackageName") String likePackageName,
                                              Pageable pageable);

    @Query(value = "SELECT CAST(A.id as varchar) id, A.name, A.version, A.supplier, A.description, A.copyright, A.summary, A.homepage, " +
            "    A.spdx_id spdxId, A.download_location downloadLocation, A.files_analyzed filesAnalyzed," +
            "    A.license_concluded licenseConcluded, A.license_declared licenseDeclared, A.source_info sourceInfo," +
            "    CAST(A.sbom_id as varchar) sbomId, B.purl" +
            "    FROM package A, external_purl_ref B" +
            "    WHERE A.id = B.pkg_id" +
            "    AND A.sbom_id = ( SELECT id FROM sbom WHERE product_id = :productId)" +
            "    AND B.category = :binaryType" +
            "    AND (:isExactly = FALSE OR (B.purl = :equalQueryPurl))" +
            "    AND (:isExactly = TRUE OR (B.purl LIKE %:likeQueryPurl% ))",
            countProjection = "1",
            nativeQuery = true)
    Page<Map> queryPackageInfoByBinary(@Param("productId") String productId,
                                       @Param("binaryType") String binaryType,
                                       @Param("isExactly") Boolean isExactly,
                                       @Param("equalQueryPurl") String equalQueryPurl,
                                       @Param("likeQueryPurl") String likeQueryPurl,
                                       Pageable pageable);

}
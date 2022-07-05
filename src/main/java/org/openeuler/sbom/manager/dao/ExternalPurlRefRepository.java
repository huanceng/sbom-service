package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExternalPurlRefRepository extends JpaRepository<ExternalPurlRef, UUID> {

    @Query(value = "SELECT * FROM external_purl_ref WHERE category = :category AND pkg_id = :pkgId",
            nativeQuery = true)
    List<ExternalPurlRef> queryPackageRef(@Param("pkgId") UUID pkgId, @Param("category") String category);
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PkgVerfCodeExcludedFileRepository extends JpaRepository<PkgVerfCodeExcludedFile, UUID> {

    @Query(value = "SELECT A.* FROM pkg_verf_code_excluded_file A, pkg_verf_code B, package C WHERE a.pkg_verf_code_id = B.id AND B.pkg_id = C.id AND C.sbom_id = :sbomId",
            nativeQuery = true)
    List<PkgVerfCodeExcludedFile> findBySbomId(UUID sbomId);
}
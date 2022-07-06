package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ExternalVulRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExternalVulRefRepository extends JpaRepository<ExternalVulRef, UUID> {

    @Query(value = "SELECT A.* FROM external_vul_ref A, package B WHERE A.pkg_id = B.id AND B.sbom_id = :sbomId",
            nativeQuery = true)
    List<ExternalVulRef> findBySbomId(UUID sbomId);

}
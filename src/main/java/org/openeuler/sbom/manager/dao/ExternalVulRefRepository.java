package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ExternalVulRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ExternalVulRefRepository extends JpaRepository<ExternalVulRef, UUID> {
    @Query(value = "select * from external_vul_ref where pkg_id = ?1 and vul_id = ?2", nativeQuery = true)
    ExternalVulRef findByPkgIdAndVulId(UUID pkgId, String vulId);
}
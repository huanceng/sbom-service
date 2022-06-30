package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExternalPurlRefRepository extends JpaRepository<ExternalPurlRef, UUID> {
    ExternalPurlRef findByPkgIdAndPurlId(UUID pkgId, UUID purlId);
}
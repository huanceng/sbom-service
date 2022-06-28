package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.PkgVerfCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PkgVerfCodeRepository extends JpaRepository<PkgVerfCode, UUID> {
    PkgVerfCode findByPkgId(UUID packageId);
}
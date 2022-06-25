package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.RawSbom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RawSbomRepository extends JpaRepository<RawSbom, UUID> {
}
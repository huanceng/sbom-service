package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Sbom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SbomRepository extends JpaRepository<Sbom, String> {
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Sbom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SbomRepository extends JpaRepository<Sbom, String> {

    Optional<Sbom> findByProductId(String productId);
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.SbomCreator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SbomCreatorRepository extends JpaRepository<SbomCreator, UUID> {
    SbomCreator findBySbomIdAndName(UUID sbomId, String name);

    List<SbomCreator> findBySbomId(UUID sbomId);
}
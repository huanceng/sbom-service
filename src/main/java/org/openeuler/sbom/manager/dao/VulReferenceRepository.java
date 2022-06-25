package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.VulReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VulReferenceRepository extends JpaRepository<VulReference, UUID> {
}
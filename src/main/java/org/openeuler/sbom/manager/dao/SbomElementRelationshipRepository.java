package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.SbomElementRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SbomElementRelationshipRepository extends JpaRepository<SbomElementRelationship, UUID> {
}
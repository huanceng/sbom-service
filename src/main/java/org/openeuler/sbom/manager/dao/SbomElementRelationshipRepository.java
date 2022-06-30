package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.SbomElementRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SbomElementRelationshipRepository extends JpaRepository<SbomElementRelationship, UUID> {
    @Query(value = "select * from sbom_element_relationship where sbom_id = ?1 and element_id = ?2 and " +
            "related_element_id = ?3 and relationship_type = ?4", nativeQuery = true)
    SbomElementRelationship findUniqueItem(String sbomId, String elementId, String RelatedElementId, String RelationshipType);
}
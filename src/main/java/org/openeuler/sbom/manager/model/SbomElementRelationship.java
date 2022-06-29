package org.openeuler.sbom.manager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "sbom_element_uk", columnList = "sbom_id, element_id, related_element_id, relationship_type", unique = true)
})
public class SbomElementRelationship {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false, name = "element_id")
    private String elementId;

    @Column(columnDefinition = "TEXT", nullable = false, name = "related_element_id")
    private String RelatedElementId;

    @Column(columnDefinition = "TEXT", nullable = false, name = "relationship_type")
    private String RelationshipType;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sbom_id", foreignKey = @ForeignKey(name = "sbom_id_fk"))
    private Sbom sbom;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getRelatedElementId() {
        return RelatedElementId;
    }

    public void setRelatedElementId(String relatedElementId) {
        RelatedElementId = relatedElementId;
    }

    public String getRelationshipType() {
        return RelationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        RelationshipType = relationshipType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Sbom getSbom() {
        return sbom;
    }

    public void setSbom(Sbom sbom) {
        this.sbom = sbom;
    }
}

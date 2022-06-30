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

/**
 * Score of a vulnerability.
 */
@Entity
@Table(indexes = {
        @Index(name = "vul_score_uk", columnList = "scoring_system, score, vector", unique = true)
})
public class VulScore {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * The name of the scoring system to express the severity of this vulnerability.
     */
    @Column(columnDefinition = "TEXT", name = "scoring_system", nullable = false)
    private String scoringSystem;

    /**
     * Score of a vulnerability calculated by the scoring system.
     */
    @Column(nullable = false)
    private Double score;

    /**
     * Vector of a vulnerability calculated by the scoring system.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String vector;

    /**
     * Vulnerability that a score belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vul_id", foreignKey = @ForeignKey(name = "vul_id_fk"))
    private Vulnerability vulnerability;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(String scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }
}

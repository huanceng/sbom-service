package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.VulScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VulScoreRepository extends JpaRepository<VulScore, UUID> {
}
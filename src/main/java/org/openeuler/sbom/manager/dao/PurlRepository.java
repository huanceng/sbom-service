package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Purl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurlRepository extends JpaRepository<Purl, UUID> {
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.PurlQualifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurlQualifierRepository extends JpaRepository<PurlQualifier, UUID> {
}
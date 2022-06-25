package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LicenseRepository extends JpaRepository<License, UUID> {
}
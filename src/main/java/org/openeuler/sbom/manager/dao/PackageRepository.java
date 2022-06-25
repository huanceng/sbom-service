package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
}
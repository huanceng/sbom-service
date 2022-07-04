package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
    List<Package> findBySpdxId(String spdxId);
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PkgVerfCodeExcludedFileRepository extends JpaRepository<PkgVerfCodeExcludedFile, UUID> {
}
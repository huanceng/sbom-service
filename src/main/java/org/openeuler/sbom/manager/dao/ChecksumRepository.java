package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Checksum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChecksumRepository extends JpaRepository<Checksum, UUID> {
}
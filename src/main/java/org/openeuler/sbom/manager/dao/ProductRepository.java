package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
}
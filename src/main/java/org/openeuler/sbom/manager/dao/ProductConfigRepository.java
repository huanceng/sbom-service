package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.ProductConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductConfigRepository extends JpaRepository<ProductConfig, UUID> {
    @Query(value = "SELECT * FROM product_config WHERE product_type = ? ORDER BY ord ASC",
            nativeQuery = true)
    List<ProductConfig> findByProductTypeOrderByOrdAsc(String productType);
}
package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT * FROM product WHERE attribute @> :attr\\:\\:jsonb and attribute <@ :attr\\:\\:jsonb",
            nativeQuery = true)
    Product queryProductByFullAttributes(@Param("attr") String attr);
}
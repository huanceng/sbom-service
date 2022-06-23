package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query(value = "select id, user_name, email from user_entity where user_name = :name", nativeQuery = true)
    List<UserEntity> findUsersByName1(@Param("name") String name);

    @Query(value = "select id, user_name, email from user_entity where user_name = ?1", nativeQuery = true)
    List<UserEntity> findUsersByName2(String name);

    List<UserEntity> findByUserName(String name);

}

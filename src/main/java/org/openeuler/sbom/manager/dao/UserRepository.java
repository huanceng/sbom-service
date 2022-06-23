package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {

    @Query(value = "select id, user_name, email from user_entity where user_name = :name", nativeQuery = true)
    List<UserEntity> findUsersByName1(@Param("name") String name);

    @Query(value = "select id, user_name, email from user_entity where user_name = ?1", nativeQuery = true)
    List<UserEntity> findUsersByName2(String name);

    List<UserEntity> findByUserName(String name);

    @Query(value = "select id, user_name, email from user_entity where user_name = :name",
            //  两个count配置的效果等价
            countProjection = "1",
            countQuery = "select count(1) from user_entity where user_name = :name",
            nativeQuery = true)
    Page<UserEntity> findUsersByNameForPage(@Param("name") String name, Pageable pageable);

    @Modifying
    @Query(value = "update user_entity set " +
            "email=:#{#user.email} \n" +
            "\t\t where user_name=:#{#user.userName}",
            nativeQuery = true)
    void updateUserEmailByUserName(UserEntity user);

}

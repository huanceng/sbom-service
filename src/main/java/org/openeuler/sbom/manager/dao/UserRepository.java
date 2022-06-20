package org.openeuler.sbom.manager.dao;

import org.openeuler.sbom.manager.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

}

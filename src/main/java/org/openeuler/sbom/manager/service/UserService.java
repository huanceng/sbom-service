package org.openeuler.sbom.manager.service;

import org.openeuler.sbom.manager.model.UserEntity;

public interface UserService {

    void addNewUserByNameAndEmail(String name, String email);

    void addNewUserByEntity(UserEntity user);

    Iterable<UserEntity> getAllUsers();

    void deleteAllUsers();
}

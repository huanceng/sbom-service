package org.openeuler.sbom.manager.service.impl;

import org.openeuler.sbom.manager.dao.UserRepository;
import org.openeuler.sbom.manager.model.UserEntity;
import org.openeuler.sbom.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addNewUserByNameAndEmail(String name, String email) {
        UserEntity n = new UserEntity();
        n.setUserName(name);
        n.setEmail(email);
        userRepository.save(n);
    }

    @Override
    public void addNewUserByEntity(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}

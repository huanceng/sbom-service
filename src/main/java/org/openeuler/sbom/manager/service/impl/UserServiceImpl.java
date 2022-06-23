package org.openeuler.sbom.manager.service.impl;

import org.openeuler.sbom.manager.model.PageVo;
import org.openeuler.sbom.manager.dao.UserRepository;
import org.openeuler.sbom.manager.model.UserEntity;
import org.openeuler.sbom.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<UserEntity> findByUserName1(String name) {
        return userRepository.findByUserName(name);
    }

    @Override
    public List<UserEntity> findByUserName2(String name) {
        return userRepository.findUsersByName1(name);
    }

    @Override
    public List<UserEntity> findByUserName3(String name) {
        return userRepository.findUsersByName2(name);
    }

    @Override
    public PageVo<UserEntity> findAllPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(new Sort.Order(Sort.Direction.DESC, "userName")));
        return new PageVo<>((PageImpl<UserEntity>) userRepository.findAll(pageable));
    }

    @Override
    public PageVo<UserEntity> findAllPageable(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(new Sort.Order(Sort.Direction.DESC, "user_name")));
        return new PageVo<>((PageImpl<UserEntity>) userRepository.findUsersByNameForPage(name, pageable));
    }
}

package org.openeuler.sbom.manager.controller;

import org.openeuler.sbom.manager.dao.UserRepository;
import org.openeuler.sbom.manager.model.UserEntity;
import org.openeuler.sbom.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/sbom")
public class SbomController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/addUser")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email) {
        userService.addNewUserByNameAndEmail(name, email);
        return "Saved";
    }

    @PostMapping(path = "/addUserRecord")
    public @ResponseBody ResponseEntity addNewUserEntity(@RequestBody UserEntity user) {
        userService.addNewUserByEntity(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Saved");
    }

    @GetMapping(path = "/allUser")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}

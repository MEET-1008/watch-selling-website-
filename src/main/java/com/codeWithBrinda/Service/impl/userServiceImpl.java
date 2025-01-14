package com.codeWithBrinda.Service.impl;

import com.codeWithBrinda.Repo.RoleRepo;
import com.codeWithBrinda.Service.UserService;
import com.codeWithBrinda.entities.Roles;
import com.codeWithBrinda.entities.User;
import com.codeWithBrinda.exception.ResouecenotfoundException;
import com.codeWithBrinda.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class userServiceImpl implements UserService {

    @Autowired(required=true)
    PasswordEncoder passwordEncoder;

    @Autowired
   private UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Override
    public void saveUsers(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println(user.getRoles() + " //////////////////////**************");
        Roles roles = this.roleRepo.findById(502).get();
        System.out.println(roles+"------------------------ ");
        user.getRoles().add(roles);
//        user.isEnabled();
        userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
       return userRepo.findByEmail(email).orElseThrow(() -> new ResouecenotfoundException("email id ","id",email));

//    return  userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User upateUser(User user) {

        User user2 = userRepo.findById(user.getUserid()).orElseThrow(() -> new ResouecenotfoundException("user","userid", user.getUserid()));
        user2.setUsername(user.getUsername());
        user2.setPassword(user.getPassword());
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setPhonenumber(user.getPhonenumber());
        user2.setProfilepic(user.getProfilepic());
        user2.setProviderUserId(user.getProviderUserId());
        user2.setProvider(user.getProvider());
        user2.setEmailverified(user.isEmailverified());
        user2.setPhoneverified(user.isPhoneverified());

        return userRepo.save(user2);
    }

    @Override
    public void deleteUser(int id) {
        User user2 = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user2);

    }

    @Override
    public boolean isUserExistsBYEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElse(null);
        return user2 != null;
    }

    @Override
    public boolean isUserExists(int id) {
      User user2 =  userRepo.findById(id).orElse(null);
        return user2 != null;
    }

    @Override
    public Optional<User> getAllUser() {
        return Optional.empty();
    }
}

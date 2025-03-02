package com.funWithStocks.FunWithStocks.services;

import com.funWithStocks.FunWithStocks.dto.MembersChildDTO;
import com.funWithStocks.FunWithStocks.dto.MembersDTO;
import com.funWithStocks.FunWithStocks.entity.User;
import com.funWithStocks.FunWithStocks.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import com.funWithStocks.FunWithStocks.dto.Role;
import com.funWithStocks.FunWithStocks.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl{

    Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User createUser(UserDTO userDTO) {
        User existingUserName = userRepository.findByUsername(userDTO.getUsername());
        if (existingUserName != null) {
            logger.info("Already user present with this username");
            throw new DuplicateRequestException("Username already exists: " + userDTO.getUsername());
            //return existingUserName;
        }else {
            User user=new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            //user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            if (userDTO.getParentId() != null) {
                User parent1 = userRepository.findById(userDTO.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid parent ID"));
                user.setParent(parent1);
            }
            return userRepository.save(user);
        }
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public Collection<GrantedAuthority> getAuthoritiesForUser(String username) {

        // Assuming you retrieve roles for the user from the database or another source
        User userRoleFetch= findUserByUsername(username);
        Set<Role> userRoles= Collections.singleton(userRoleFetch.getRole());

        // Convert roles to GrantedAuthority objects
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors
                        .toList());
    }

    public MembersDTO getUserWithChildren(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found by username : " + username);
        }
        //User user = userServiceImpl.getUserWithChildren(username);
        System.out.println(user.getUsername());
        //Setting up parents and children response DTO
        MembersDTO membersDTO=new MembersDTO();
        membersDTO.setUsername(user.getUsername());
        List<MembersChildDTO> childrenDTO = user.getChildren().stream()
                .map(child -> {
                    MembersChildDTO membersChildDTO=new MembersChildDTO();
                    membersChildDTO.setId(child.getId());
                    membersChildDTO.setUsername(child.getUsername());
                    return membersChildDTO;
                })
                .toList();
        membersDTO.setChildren(childrenDTO);
        return membersDTO;
    }

    public void deductUserBalance(String username, double withdrawalAmount) {
        logger.info("Inside deductUserBalance method ");
        // Retrieve user from the database
        User user = userRepository.findByUsername(username);
        logger.info("User is: {} , with balance: {}", user.getUsername(),user.getAccountBalance());

        // If user is found and has sufficient balance, deduct the withdrawal amount
        if (user.getAccountBalance() >= withdrawalAmount) {
            logger.info("Second level balance check done ");
            double newBalance = user.getAccountBalance() - withdrawalAmount;
            logger.info("Updated account balance : Rs. {}",newBalance);
            user.setAccountBalance(newBalance);
            userRepository.save(user);
            //return true; // Deduction successful
        }
        //return false; // Insufficient balance or user not found
    }
}
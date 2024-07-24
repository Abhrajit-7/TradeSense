package com.funWithStocks.FunWithStocks.config;

import com.funWithStocks.FunWithStocks.dto.Role;
import com.funWithStocks.FunWithStocks.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class Authorities {
    User user=new User();
    Collection<Role> roles = Collections.singleton(user.getRole());
    Collection<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.name()))
            .collect(Collectors.toList());
}

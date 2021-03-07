package com.spring.taskmanger.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.taskmanger.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private int age;

    private String token;

    //User user;

    /*public CustomUserDetails(User user) {
        this.user = user;
    }

     */

    public CustomUserDetails(Long id, String name, String email, String password,
                           int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static CustomUserDetails build(User user1) {
        /*
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

         */

        return new CustomUserDetails(
                user1.getId(),
                user1.getName(),
                user1.getEmail(),
                user1.getPassword(),
                user1.getAge());
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
       return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomUserDetails user = (CustomUserDetails) o;
        return Objects.equals(id, user.id);
    }
}

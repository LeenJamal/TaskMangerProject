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

    private User user;


    public CustomUserDetails(Long id, String name, String email, String password,
                           int age, User user) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.user = user;
    }

    public static CustomUserDetails build(User user1) {
        return new CustomUserDetails(
                user1.getId(),
                user1.getName(),
                user1.getEmail(),
                user1.getPassword(),
                user1.getAge(),
                user1);
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

    public User getUser() { return user; }

    public void setUser(User user) {this.user = user; }

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
        if (this == o) // / If the object is compared with itself then return true
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomUserDetails user = (CustomUserDetails) o;
        return Objects.equals(id, user.id);
    }
}

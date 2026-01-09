package com.DOCKin.global.security.auth;

import com.DOCKin.dto.CustomUserInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@NullMarked
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final CustomUserInfoDto member;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_"+member.getRole().toString());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserId();
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
}
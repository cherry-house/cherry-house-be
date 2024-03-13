package com.cherryhouse.server._core.security;

import com.cherryhouse.server.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails{

    private Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String email, String password, Collection<? extends GrantedAuthority> authorities){
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //해당 유저의 권한 리턴
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_GUEST"));

        return new UserPrincipal(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
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
}

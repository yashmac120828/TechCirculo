//package org.yug.backend.security;
//
//import  org.yug.backend.model.User;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.UUID;
//
//@Data
//public class UserPrincipal implements UserDetails {
//    private UUID id;
//    private String email;
//    private String password;
//    private String role;
//
//    public UserPrincipal(UUID id, String email, String password, String role) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null; // You can implement roles/authorities here if needed
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public static UserPrincipal create(User user) {
//        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(), user.getRole().name());
//    }
//}

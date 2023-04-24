package com.hardtech.security.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hardtech.app.entities.Task;
import com.hardtech.app.entities.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@DynamicInsert
@DynamicUpdate
@Entity(name = "user_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    Integer id;
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;

    @NotBlank
    @Column(unique = true)
    String email;

    @JsonIgnore
    String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    List<Role> roles;

    boolean isEnabled;

    @ManyToMany(mappedBy = "members")
    List<Team> teams = new ArrayList<>();


    @ManyToMany(mappedBy = "members")
    List<Task> tasks = new ArrayList<>();

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updateAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.name()))
        );
        return grantedAuthorities;
        //return List.of(new SimpleGrantedAuthority(role.name()));
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
        return this.isEnabled;
    }
}
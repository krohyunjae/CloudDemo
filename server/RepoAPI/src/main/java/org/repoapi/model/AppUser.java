package org.repoapi.model;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;
@Entity
@Table(name="app_user")
public class AppUser extends User {
    private static final String AUTHORITY_PREFIX = "ROLE_";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;

    @OneToMany(targetEntity = Meta.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appUser")
    private List<Meta> meta = new ArrayList<>();
    public AppUser(){
        this(0l,"email", "", "firstName", "lastName", false,List.of(), List.of());
    }
    public AppUser(long id, String email, String password, String firstName, String lastName, boolean disabled, List<String> roles, List<Meta> meta){
        super(email, password, !disabled, true, true, true, convertRolesToAuthorities(roles));
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.meta = meta;
    }
    public void addMeta(Meta path){
        meta.add(path);
        path.setAppUser(this);
    }
    public void removeMeta(Meta path){
        meta.remove(path);
        path.setAppUser(null);
    }
    @JsonIgnore
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @JsonIgnore
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Meta> getMeta() {
        return meta;
    }

    public void setMeta(List<Meta> meta) {
        this.meta = meta;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }
    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles){
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for(String role: roles){
            if(!role.startsWith(AUTHORITY_PREFIX)){
                role = AUTHORITY_PREFIX + role;
            }
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities){
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }

}

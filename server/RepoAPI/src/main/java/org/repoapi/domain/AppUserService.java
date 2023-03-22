package org.repoapi.domain;

import org.repoapi.exception.ValidationException;
import org.repoapi.model.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public interface AppUserService extends UserDetailsService {
    AppUser createAppUser(String email, String password, String firstName, String lastName)throws ValidationException;
    AppUser updateAppUser(String id, String email, String password, String firstName, String lastName)throws ValidationException;
    boolean deleteAppUserById(String id, String email) throws UsernameNotFoundException;
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}

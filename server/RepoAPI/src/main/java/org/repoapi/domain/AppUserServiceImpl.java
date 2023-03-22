package org.repoapi.domain;

import org.repoapi.data.AppUserRepository;
import org.repoapi.data.MetaRepository;
import org.repoapi.exception.ValidationException;
import org.repoapi.model.AppUser;
import org.repoapi.model.Meta;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AppUserServiceImpl implements AppUserService{
    private final AppUserRepository appUserRepository;
    private final MetaRepository metaRepository;

    private final PasswordEncoder encoder;
    public AppUserServiceImpl(AppUserRepository appUserRepository, MetaRepository metaRepository, PasswordEncoder encoder){
        this.appUserRepository = appUserRepository;
        this.metaRepository = metaRepository;
        this.encoder = encoder;
    }


    @Override
    public AppUser createAppUser(String email, String password, String firstName, String lastName) throws ValidationException {
        validateNull(email, password, firstName, lastName);
        password = encoder.encode(password);
        AppUser appUser = new AppUser(0l,email, password, firstName, lastName, false, List.of("USER"), List.of());
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser updateAppUser(String id, String email, String password, String firstName, String lastName) throws ValidationException, UsernameNotFoundException {
        validateNull(email, password, firstName, lastName);
        password = encoder.encode(password);
        AppUser appUser = appUserRepository.findAppUserByEmail(email);
        if(appUser == null) throw new UsernameNotFoundException("No user with such email");
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setPassword(password);
        return appUserRepository.save(appUser);
    }

    @Override
    public boolean deleteAppUserById(String id, String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByEmail(email);
        if(appUser == null) throw new UsernameNotFoundException("No user with such email");
        try{
            appUserRepository.delete(appUser);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByEmail(email);
        if(appUser == null || !appUser.isEnabled()){
            throw new UsernameNotFoundException("No user with such email");
        }
        return appUser;
    }
    public List<Meta> findMetaByAppUser(AppUser appUser){
        return metaRepository.findByAppUser(appUser);
    }

    public Meta findById(Long id){
        return metaRepository.findById(id).orElse(null);
    }

    public boolean deleteMeta(Meta meta){
        try{
            metaRepository.delete(meta);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Meta updateMeta(Meta meta){
        try{
            metaRepository.save(meta);
            return meta;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private void validateNull(String email, String password, String firstName, String lastName) throws ValidationException{
        checkField(email, "Email");
        checkField(password, "Password");
        checkField(firstName, "First name");
        checkField(lastName, "Last name");
    }
    private void checkField(String field, String name) throws ValidationException{
        if(field == null || field.isBlank()) throw new ValidationException(name + " cannot be empty");
    }
}

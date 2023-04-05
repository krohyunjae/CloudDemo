package org.repoapi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.repoapi.domain.AppUserService;
import org.repoapi.model.AppUser;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repo/account")
public class AppUserController {
    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<?> createAppUser(@RequestBody Map<String, String> body){
        try{
            AppUser appUser = appUserService.createAppUser(body.get("username"), body.get("password"), body.get("firstName"), body.get("lastName"));
            return new ResponseEntity<>(appUser.getEmail(), HttpStatus.CREATED);
        } catch (BindValidationException e) {
            return new ResponseEntity<>(List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAppUser(@RequestBody Map<String, String> body){
        try{
            AppUser appUser = appUserService.updateAppUser(body.get("id"),body.get("username"), body.get("password"), body.get("firstName"), body.get("lastName"));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BindValidationException e) {
            return new ResponseEntity<>(List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAppUser(@RequestBody Map<String, String> body){
        try{
            if(appUserService.deleteAppUserById(body.get("id"), body.get("username"))){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
            }
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        try{
            request.logout();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServletException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/name")
    public Map<String, Object> getUserName(@AuthenticationPrincipal OAuth2User principal){
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }


}

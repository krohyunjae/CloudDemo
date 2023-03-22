package org.repoapi.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name="meta")
public class Meta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = AppUser.class, fetch=FetchType.LAZY)
//    @JoinColumn(name="user_id")
    private AppUser appUser;


    private String path;

    public Meta() {
    }

    public Meta(Long id, String path, AppUser appUser) {
        this.id = id;
        this.path = path;
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meta meta = (Meta) o;
        return Objects.equals(id, meta.id) && Objects.equals(appUser, meta.appUser) && Objects.equals(path, meta.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appUser, path);
    }
}

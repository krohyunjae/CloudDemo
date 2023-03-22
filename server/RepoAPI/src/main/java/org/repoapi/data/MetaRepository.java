package org.repoapi.data;

import org.repoapi.model.AppUser;
import org.repoapi.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

    Optional<Meta> findById(Long id);

    List<Meta> findByAppUser(AppUser appUser);

}

package com.ecocursos.ecocursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecocursos.ecocursos.models.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
}

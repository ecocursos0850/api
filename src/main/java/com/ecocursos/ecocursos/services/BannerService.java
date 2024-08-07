package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Banner;
import com.ecocursos.ecocursos.models.BannerSite;
import com.ecocursos.ecocursos.repositories.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository repository;

    public List<Banner> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ErrorException("");
        }
    }

    public Banner listarById(Integer id) {
        try  {
            return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Banner não encontrado"));
        } catch (Exception e) {
            throw new ErrorException("Erro ao listar banner por ID");
        }
    }

    public Banner salvar(Banner banner) {
        try {
            return repository.save(banner);
        } catch (Exception e) {
            throw new ErrorException("Erro ao salvar banner");
        }
    }

    public Banner alterar(Integer id, Banner site) {
        try {
            Banner  bannerSiteExistente = listarById(id);
            site.setId(id);
            return repository.save(site);
        } catch (Exception e) {
            throw new ErrorException("Erro ao alterar banner do site");
        }
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}

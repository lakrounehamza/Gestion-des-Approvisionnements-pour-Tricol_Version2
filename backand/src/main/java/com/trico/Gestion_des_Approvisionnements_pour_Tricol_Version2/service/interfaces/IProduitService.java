package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IProduitService {
    ProduitDto save(ProduitRegisterDto dto);
    ProduitDto update(Long id, ProduitRegisterDto produit);
    ProduitDto delete(Long id);
    ProduitDto getById(Long id);
    Page<ProduitDto> getAll(Pageable pageable);
    Page<ProduitDto> getByCategorie(String categorie, Pageable pageable);
    Page<ProduitDto> search(String nom, String reference, Pageable pageable);
}

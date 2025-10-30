package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;

import java.util.List;

public interface IProduitService {
    ProduitDto save(ProduitRegisterDto dto);
    ProduitDto update(Long id , ProduitRegisterDto produit);
    ProduitDto detete(Long id);
    ProduitDto getById(Long id);
    List<ProduitDto> getAll();
}

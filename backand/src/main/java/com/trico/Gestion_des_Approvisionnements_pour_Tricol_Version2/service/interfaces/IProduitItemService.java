package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;

import java.util.List;

public interface IProduitItemService {
    ProduitItemDto save(ProduitItemRegisterDto produitItemRegisterDto);
    ProduitItemDto update(ProduitItemRegisterDto produitItemRegisterDto ,Long id);
    ProduitItemDto delete(Long id);
    List<ProduitItemDto> findAll();
    List<ProduitItemDto> findByProduitId(Long id);
    List<ProduitItemDto> findById(Long id);

}

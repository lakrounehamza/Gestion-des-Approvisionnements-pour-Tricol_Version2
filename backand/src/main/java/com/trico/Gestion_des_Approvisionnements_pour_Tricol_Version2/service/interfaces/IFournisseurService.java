package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;

import  java.util.List;
public interface IFournisseurService {
    FournisseurDto save(FournisseurRegistreDTO fournisseurDto);
    FournisseurDto update(Long id ,FournisseurRegistreDTO fournisseurDto);
    FournisseurDto delete(Long id);
    FournisseurDto getById(Long id);
    List<FournisseurDto>  getAll();
}

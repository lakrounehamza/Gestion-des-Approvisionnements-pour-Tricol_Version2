package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import  java.util.List;
public interface IFournisseurService {
    FournisseurDto save(FournisseurDto fournisseurDto);
    FournisseurDto update(Long id ,FournisseurDto fournisseurDto);
    FournisseurDto delete(Long id);
    FournisseurDto getByid(Long id);
    List<FournisseurDto>  getAll();
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import  java.util.List;
public interface IFournisseurService {
    FournisseurDto save(FournisseurDto fournisseurDto);
    FournisseurDto update(String id ,FournisseurDto fournisseurDto);
    FournisseurDto delete(String id);
    FournisseurDto getByid(String id);
    List<FournisseurDto>  getAll();
}

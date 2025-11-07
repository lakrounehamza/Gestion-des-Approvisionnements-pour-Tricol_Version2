package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import  java.util.List;
public interface IFournisseurService {
    FournisseurDto save(FournisseurRegistreDTO fournisseurDto);
    FournisseurDto update(Long id, FournisseurRegistreDTO fournisseurDto);
    FournisseurDto delete(Long id);
    FournisseurDto getById(Long id);
    Page<FournisseurDto> getAll(Pageable pageable);
}

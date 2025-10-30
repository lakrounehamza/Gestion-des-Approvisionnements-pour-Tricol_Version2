package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.maper;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseuRregistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FournisseurMaper {
    Fournisseur  toEntity(FournisseurDto dto);
    Fournisseur  toEntity(FournisseuRregistreDTO dto);
    FournisseurDto toDto(Fournisseur entity);
}

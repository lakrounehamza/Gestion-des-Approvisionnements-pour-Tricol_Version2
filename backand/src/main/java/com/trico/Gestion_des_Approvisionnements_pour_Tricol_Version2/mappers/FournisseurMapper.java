package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FournisseurMapper {
    Fournisseur toEntity(FournisseurDto dto);
    Fournisseur toEntity(FournisseurRegistreDTO dto);
    FournisseurDto toDto(Fournisseur entity);
}

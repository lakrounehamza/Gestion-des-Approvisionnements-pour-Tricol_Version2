package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    Produit toEntity(ProduitRegisterDto produitRegisterDto);

    Produit toEntity(ProduitDto produitDto);

    ProduitDto toDto(Produit produit);
}

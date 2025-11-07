package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitItemMapper {
    ProduitItem toEntity(ProduitItemDto produitItemDto);
    ProduitItemDto toDto(ProduitItem produitItem);
    ProduitItem toEntity(ProduitItemRegisterDto produitItemRegisterDto);
}

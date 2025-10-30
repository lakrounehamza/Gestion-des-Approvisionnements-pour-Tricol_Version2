package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;

public interface CommandeItemMapper {
    CommandeItem toEntity(CommandeItemDto commandeItemDto);
    CommandeItem toEntity(CommandeItemRegisterDto commandeItemRegisterDto);
    CommandeItemDto toDto(CommandeItem commandeItem);
}

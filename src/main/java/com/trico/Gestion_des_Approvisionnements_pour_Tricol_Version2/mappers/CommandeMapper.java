package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;

public interface CommandeMapper {
    Commande toEntity(CommandeDto dto);
    Commande toEntity(CommandeRegisterDto dto);
    CommandeDto toDto(Commande entity);
}

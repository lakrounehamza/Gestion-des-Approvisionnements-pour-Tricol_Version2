package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    Commande toEntity(CommandeDto dto);
    Commande toEntity(CommandeRegisterDto dto);
    @Mapping(source = "client", target = "client")
    @Mapping(source = "fournisseur", target = "fournisseur")
    CommandeDto toDto(Commande entity);
}

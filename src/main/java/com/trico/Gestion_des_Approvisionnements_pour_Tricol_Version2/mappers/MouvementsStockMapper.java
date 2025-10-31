package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MouvementsStockMapper {
    MouvementsStock toEntity(MouvementsStockDto mouvementsStockDto);
    MouvementsStock toEntity(MouvementsStockRegisterDto mouvementsStockRegisterDto);
    MouvementsStockDto  toDto(MouvementsStock  entity);
}

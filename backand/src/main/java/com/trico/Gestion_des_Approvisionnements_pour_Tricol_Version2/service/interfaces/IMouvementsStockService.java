package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;

import java.util.List;

public interface IMouvementsStockService {
    MouvementsStockDto save(MouvementsStockRegisterDto dto);
    MouvementsStockDto update(Long id , MouvementsStockRegisterDto produit);
    MouvementsStockDto delete(Long id);
    MouvementsStockDto getById(Long id);
    List<MouvementsStockDto> getAll();
    List<MouvementsStockDto> getByProduitId(Long id);
}

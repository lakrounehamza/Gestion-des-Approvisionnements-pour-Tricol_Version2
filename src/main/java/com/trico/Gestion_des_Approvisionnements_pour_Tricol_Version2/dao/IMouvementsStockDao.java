package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMouvementsStockDao extends JpaRepository<MouvementsStock,Long> {
}

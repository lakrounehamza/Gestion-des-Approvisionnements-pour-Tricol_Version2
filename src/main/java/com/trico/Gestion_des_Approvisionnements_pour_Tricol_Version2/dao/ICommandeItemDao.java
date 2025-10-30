package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommandeItemDao extends JpaRepository<CommandeItem,Long> {
}

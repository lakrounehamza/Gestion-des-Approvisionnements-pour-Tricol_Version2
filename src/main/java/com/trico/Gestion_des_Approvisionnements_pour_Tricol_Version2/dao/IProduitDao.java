package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProduitDao extends JpaRepository<Produit,Long> {
}

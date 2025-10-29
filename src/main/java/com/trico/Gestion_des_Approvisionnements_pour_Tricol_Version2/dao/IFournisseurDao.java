package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFournisseurDao extends JpaRepository<Fournisseur,String> {
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitDto {
    private Long id;
    private String nom;
    private double prix;
    private String description;
    private CategorieEnum categorie;
    private List<CommandeItem> CommandeItem;
    private List<MouvementsStock> mouvementsStock;
}
package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.TypeMouvementEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MouvementsStockDto {
    private Long id;
    private Date datemouvement ;
    private int quantity  ;
    private int quantityMin ;
    private TypeMouvementEnum type ;
    private String reference ;
    private Produit produit;
}
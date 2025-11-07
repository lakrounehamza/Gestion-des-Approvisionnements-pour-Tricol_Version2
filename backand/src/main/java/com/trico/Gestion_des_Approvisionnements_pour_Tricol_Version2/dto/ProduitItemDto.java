package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitItemDto {
    private Long id;
    private double prix;
    private int quantite;
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandeItemRegisterDto {
    private int quantity;
    private Date date;
    private Commande commande;
    private Produit produit;
}
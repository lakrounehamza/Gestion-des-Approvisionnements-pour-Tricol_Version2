package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandeRegisterDto {
    private StatutCommandeEnum statut;
    private Double montant;
    private Date dateCommande;
    private List<CommandeItem> CommandeItem;
    private Fournisseur fournisseur;
    private Fournisseur clinet;
}
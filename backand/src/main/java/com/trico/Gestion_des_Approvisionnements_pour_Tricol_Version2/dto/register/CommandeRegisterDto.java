package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Le statut de la commande est obligatoire.")
    private StatutCommandeEnum statut;

    @Positive
    @NotNull(message = "Le montant ne peut pas être nul.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à 0.")
    private Double montant;

    @NotNull(message = "La date de commande est obligatoire.")
    @PastOrPresent(message = "La date de commande ne peut pas être dans le futur.")
    private Date dateCommande;

    @NotEmpty(message = "La commande doit contenir au moins un article.")
    @Valid
    private List<CommandeItem> commandeItems;

    @NotNull(message = "Le fournisseur est obligatoire.")
    private Fournisseur fournisseur;

    @NotNull(message = "Le client est obligatoire.")
    private Fournisseur client;
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @Min(value = 1, message = "La quantité doit être au minimum de 1.")
    private int quantity;

    @NotNull(message = "La date ne peut pas être nulle.")
    @PastOrPresent(message = "La date ne peut pas être dans le futur.")
    private Date date;

    @NotNull(message = "La commande ne peut pas être nulle.")
    private Commande commande;

    @NotNull(message = "Le produit ne peut pas être nul.")
    private Produit produit;
}

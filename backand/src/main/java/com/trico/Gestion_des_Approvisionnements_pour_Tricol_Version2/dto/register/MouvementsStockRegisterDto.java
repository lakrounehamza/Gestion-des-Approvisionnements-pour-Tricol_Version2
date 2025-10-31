package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.TypeMouvementEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class MouvementsStockRegisterDto {

    @NotNull(message = "La date du mouvement est obligatoire.")
    @PastOrPresent(message = "La date du mouvement ne peut pas être dans le futur.")
    private Date datemouvement;

    @Min(value = 1, message = "La quantité doit être supérieure à 0.")
    private int quantity;

    @Min(value = 0, message = "La quantité minimale ne peut pas être négative.")
    private int quantityMin;

    @NotNull(message = "Le type de mouvement est obligatoire.")
    private TypeMouvementEnum type;

    @NotBlank(message = "La référence du mouvement est obligatoire.")
    private String reference;

    @NotNull(message = "Le produit associé est obligatoire.")
    private Produit produit;
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitRegisterDto {

    @NotBlank(message = "Le nom du produit est obligatoire.")
    @Size(min = 2, max = 100, message = "Le nom du produit doit contenir entre 2 et 100 caractères.")
    private String nom;

    @NotNull(message = "Le prix du produit est obligatoire.")
    @DecimalMin(value = "0.1", message = "Le prix doit être supérieur à 0.")
    private double prix;
    @Positive
    private int quantite;

    @NotBlank(message = "La description du produit est obligatoire.")
    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères.")
    private String description;

    @NotNull(message = "La catégorie du produit est obligatoire.")
    private CategorieEnum categorie;

    @Valid
    private List<CommandeItem> commandeItems;

    @Valid
    private List<MouvementsStock> mouvementsStock;
    @Valid
    private List<ProduitItem> produits;
}

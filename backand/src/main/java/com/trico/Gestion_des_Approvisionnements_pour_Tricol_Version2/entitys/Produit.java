package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produit {
    @Id
    private Long id;
    private String nom;
    private double prix;
    private String description;
    private CategorieEnum categorie;
    @OneToMany
    private List<CommandeItem> CommandeItem;
    @OneToMany
    private List<MouvementsStock> mouvementsStock;

}

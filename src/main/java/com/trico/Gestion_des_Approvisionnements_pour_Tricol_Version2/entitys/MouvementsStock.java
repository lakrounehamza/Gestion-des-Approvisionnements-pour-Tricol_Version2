package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.TypeMouvementEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementsStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id  ;
    private Date datemouvement ;
    private int quantity  ;
    private int quantityMin ;
    private TypeMouvementEnum type ;
    private String reference ;
    @ManyToOne
    private Produit produit;
}

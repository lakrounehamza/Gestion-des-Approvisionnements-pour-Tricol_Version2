package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
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
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String societe;
    private String adresse;
    private String contact;
    private String email;
    private String phone;
    private String ville;
    private String ice;
    @OneToMany
    private List<Commande> commande;
}

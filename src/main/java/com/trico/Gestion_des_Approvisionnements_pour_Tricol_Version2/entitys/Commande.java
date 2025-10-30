package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatutCommandeEnum statut;
    private Double montant;
    private Date dateCommande;
    @OneToMany
    private List<CommandeItem> CommandeItem;
    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
    @ManyToOne
    @JoinColumn(name = "clinet_id")
    private Fournisseur clinet;
}

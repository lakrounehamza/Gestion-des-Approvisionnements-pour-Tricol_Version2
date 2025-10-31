package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class CommandeItem {
    @Id
    private Long id;
    private int quantity;
    private Date date;
    @OneToOne
    private Commande commande;
    @ManyToOne
    private Produit produit;
}

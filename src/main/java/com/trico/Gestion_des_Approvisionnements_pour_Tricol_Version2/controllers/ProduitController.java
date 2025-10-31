package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/produits")
public class ProduitController {

    private final IProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitDto> createProduit(@Valid @RequestBody ProduitRegisterDto dto) {
        ProduitDto savedProduit = produitService.save(dto);
        return ResponseEntity.ok(savedProduit);
    }

    @GetMapping
    public ResponseEntity<List<ProduitDto>> getAllProduits() {
        List<ProduitDto> produits = produitService.getAll();
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé.");
        }
        return ResponseEntity.ok(produits);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProduitDto> getProduitById(@PathVariable Long id) {
        ProduitDto produit = produitService.getById(id);
        if (produit == null) {
            throw new NotFoundException("Produit introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(produit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProduitDto> deleteProduit(@PathVariable Long id) {
        ProduitDto deletedProduit = produitService.delete(id);
        if (deletedProduit == null) {
            throw new NotFoundException("Impossible de supprimer : produit introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedProduit);
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<ProduitDto>> getProduitsByCategorie(@PathVariable String categorie) {
        List<ProduitDto> produits = produitService.getByCategorie(categorie);
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé pour la catégorie : " + categorie);
        }
        return ResponseEntity.ok(produits);
    }
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<ProduitDto>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProduitDto> produits = produitService.getAll(pageable);
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

    @PutMapping("{id}")
    public ResponseEntity<ProduitDto> updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRegisterDto dto) {
        ProduitDto updatedProduit = produitService.update(id, dto);
        return ResponseEntity.ok(updatedProduit);
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<Page<ProduitDto>> getProduitsByCategorie(
            @PathVariable String categorie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProduitDto> produits = produitService.getByCategorie(categorie, pageable);
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé pour la catégorie : " + categorie);
        }
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProduitDto>> searchProduits(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String reference,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProduitDto> produits = produitService.search(nom, reference, pageable);
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé pour les critères de recherche.");
        }
        return ResponseEntity.ok(produits);
    }
}
package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements IProduitService {

    private final ProduitMapper produitMapper;
    private final IProduitDao produitDao;
    private final IProduitItemService produitItemService;
    private final IMouvementsStockService mouvementsStockService;
    private final ProduitItemMapper produitItemMapper;

    @Override
    public ProduitDto save(ProduitRegisterDto dto) {
        Produit produit = produitMapper.toEntity(dto);
        Produit saved = produitDao.save(produit);
        return produitMapper.toDto(saved);
    }

    @Override
    public ProduitDto update(Long id, ProduitRegisterDto dto) {
        Produit existingProduit = produitDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le produit avec l'ID " + id + " n'existe pas pour la modification."));

        existingProduit.setNom(dto.getNom());
        existingProduit.setDescription(dto.getDescription());
        existingProduit.setCategorie(dto.getCategorie());
        // Update other fields as needed based on your entity

        Produit updated = produitDao.save(existingProduit);
        return produitMapper.toDto(updated);
    }

    @Override
    public ProduitDto delete(Long id) {
        Produit produit = produitDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le produit avec l'ID " + id + " n'existe pas pour la suppression."));
        produitDao.delete(produit);
        return produitMapper.toDto(produit);
    }

    @Override
    public ProduitDto getById(Long id) {
        Produit produit = produitDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le produit avec l'ID " + id + " n'existe pas."));
        return produitMapper.toDto(produit);
    }

    @Override
    public Page<ProduitDto> getAll(Pageable pageable) {
        Page<Produit> produits = produitDao.findAll(pageable);
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit disponible.");
        }
        return produits.map(produitMapper::toDto);
    }

    @Override
    public Page<ProduitDto> getByCategorie(String categorie, Pageable pageable) {
        CategorieEnum categorieEnum;
        try {
            categorieEnum = CategorieEnum.valueOf(categorie.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Catégorie invalide : " + categorie);
        }

        Page<Produit> produits = produitDao.findByCategorie(categorieEnum, pageable);
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé pour la catégorie : " + categorie);
        }
        return produits.map(produitMapper::toDto);
    }

    @Override
    public Page<ProduitDto> search(String nom, String reference, Pageable pageable) {
        Page<Produit> produits;

        if (nom != null && reference != null) {
            // Search by both nom and description (since reference doesn't exist)
            produits = produitDao.searchByNomAndDescription(nom, reference, pageable);
        } else if (nom != null) {
            // Search by nom only
            produits = produitDao.findByNomContainingIgnoreCase(nom, pageable);
        } else if (reference != null) {
            // Search by description (or just return all if you prefer)
            produits = produitDao.searchByNom(reference, pageable);
        } else {
            // No search criteria, return all
            produits = produitDao.findAll(pageable);
        }

        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé pour les critères de recherche.");
        }
        return produits.map(produitMapper::toDto);
    }
}
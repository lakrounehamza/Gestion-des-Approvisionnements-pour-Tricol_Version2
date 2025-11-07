package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IMouvementsStockDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements IProduitService {

    private final ProduitMapper produitMapper;
    private final IProduitDao produitDao;
    private final IProduitItemService produitItemService;
    private final IMouvementsStockService  mouvementsStockService;
    private final ProduitItemMapper produitItemMapper;

    @Override
    public ProduitDto save(ProduitRegisterDto dto) {
        Produit produit = produitMapper.toEntity(dto);
        Produit saved = produitDao.save(produit);
        return produitMapper.toDto(saved);
    }

    @Override
    public ProduitDto update(Long id, ProduitRegisterDto dto) {
        Produit existingProduit = produitDao.findById(id).orElseThrow(() -> new NotFoundException("Le produit avec l’ID " + id + " n’existe pas pour la modification."));

        existingProduit.setNom(dto.getNom());
        //existingProduit.setPrix(dto.getPrix());
        existingProduit.setDescription(dto.getDescription());
        existingProduit.setCategorie(dto.getCategorie());
        Produit updated = produitDao.save(existingProduit);
        return produitMapper.toDto(updated);
    }

    @Override
    public ProduitDto delete(Long id) {
        Produit produit = produitDao.findById(id).orElseThrow(() -> new NotFoundException("Le produit avec l’ID " + id + " n’existe pas pour la suppression."));
        produitDao.delete(produit);
        //return "le produit  il a  soprimer";
        return produitMapper.toDto(produit);
    }

    @Override
    public ProduitDto getById(Long id) {
        Produit produit = produitDao.findById(id).orElseThrow(() -> new NotFoundException("Le produit avec l’ID " + id + " n’existe pas."));
        return produitMapper.toDto(produit);
    }

    @Override
    public List<ProduitDto> getAll() {
        List<Produit> produits = produitDao.findAll();
        if (produits.isEmpty()) {
            throw new NotFoundException("Aucun produit disponible.");
        }
        return produits.stream().map(p->(ProduitDto)produitMapper.toDto(p)).collect(Collectors.toList());
    }

    @Override
    public List<ProduitDto> getByCategorie(String categorie) {
        CategorieEnum categorieEnum = CategorieEnum.valueOf(categorie);
        return getAll().stream().filter(produitDto -> produitDto.getCategorie().equals(categorieEnum)).collect(Collectors.toList());
    }
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Data
public class ProduitItemService implements IProduitItemService {

    private final ProduitItemMapper produitItemMapper;
    private final IProduitItemDao produitItemDao;
    private final IProduitDao produitDao;
    private final ProduitMapper produitMapper;

    @Override
    public ProduitItemDto save(ProduitItemRegisterDto dto) {
        if (dto.getProduit() == null || dto.getProduit().getId() == null) {
            throw new IllegalArgumentException("L'ID du produit ne peut pas être null");
        }

        Produit produit = produitDao.findById(dto.getProduit().getId())
                .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + dto.getProduit().getId()));

        ProduitItem produitItem = produitItemMapper.toEntity(dto);
        produitItem.setProduit(produit);
        ProduitItem saved = produitItemDao.save(produitItem);
        return produitItemMapper.toDto(saved);
    }

    @Override
    public ProduitItemDto update(ProduitItemRegisterDto dto, Long id) {
        ProduitItem existingProduitItem = produitItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("ProduitItem introuvable avec l'ID : " + id));

        // Update fields from DTO
        if (dto.getProduit() != null && dto.getProduit().getId() != null) {
            Produit produit = produitDao.findById(dto.getProduit().getId())
                    .orElseThrow(() -> new NotFoundException("Produit introuvable avec l'ID : " + dto.getProduit().getId()));
            existingProduitItem.setProduit(produit);
        }

        existingProduitItem.setQuantite(dto.getQuantite());
        existingProduitItem.setPrix(dto.getPrix());

        ProduitItem updated = produitItemDao.save(existingProduitItem);
        return produitItemMapper.toDto(updated);
    }

    @Override
    public ProduitItemDto delete(Long id) {
        ProduitItem produitItem = produitItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("ProduitItem introuvable avec l'ID : " + id));

        produitItemDao.delete(produitItem);
        return produitItemMapper.toDto(produitItem);
    }

    @Override
    public ProduitItemDto findById(Long id) {
        ProduitItem produitItem = produitItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("ProduitItem introuvable avec l'ID : " + id));
        return produitItemMapper.toDto(produitItem);
    }

    @Override
    public Page<ProduitItemDto> findAll(Pageable pageable) {
        Page<ProduitItem> produitItems = produitItemDao.findAll(pageable);
        if (produitItems.isEmpty()) {
            throw new NotFoundException("Aucun ProduitItem trouvé.");
        }
        return produitItems.map(produitItemMapper::toDto);
    }

    @Override
    public Page<ProduitItemDto> findByProduitId(Long id, Pageable pageable) {
        Page<ProduitItem> produitItems = produitItemDao.findByProduitId(id, pageable);
        if (produitItems.isEmpty()) {
            throw new NotFoundException("Aucun ProduitItem trouvé pour le produit avec l'ID : " + id);
        }
        return produitItems.map(produitItemMapper::toDto);
    }
}
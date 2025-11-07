package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.ICommandeItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Data
public class ProduitItemService implements IProduitItemService {
    private final ProduitItemMapper produitItemMapper ;
    private final IProduitItemDao produitItemDao ;
    private final IProduitDao produitDao ;
    private final ProduitMapper produitMapper;

    @Override
    public ProduitItemDto save(ProduitItemRegisterDto dto) {
        if (dto.getProduit() == null) {
            throw new IllegalArgumentException("L'ID du produit ne peut pas être null");
        }

        Produit produit = produitDao.findById(dto.getProduit().getId())
                .orElseThrow(() -> new EntityNotFoundException("Produit introuvable avec l'ID : " + dto.getProduit().getId()));
        ProduitItem produitItem = produitItemMapper.toEntity(dto);
        produitItem.setProduit(produit);
        ProduitItem saved = produitItemDao.save(produitItem);
        return produitItemMapper.toDto(saved);
    }


    @Override
    public ProduitItemDto update(ProduitItemRegisterDto produitItemRegisterDto, Long id) {
        if(produitItemDao.findById(id).isPresent()){
           ProduitItem produitItem = produitItemDao.findById(id).get();
           produitItem.setProduit(produitItem.getProduit());
           produitItem.setQuantite(produitItem.getQuantite());
           produitItem.setPrix(produitItem.getPrix());
           produitItemDao.save(produitItem);
           return produitItemMapper.toDto(produitItem);
        }
        return null;
    }

    @Override
    public ProduitItemDto delete(Long id) {
        if(produitItemDao.findById(id).isPresent()){
            ProduitItem produitItem = produitItemDao.findById(id).get();
            produitItemDao.delete(produitItem);
            return produitItemMapper.toDto(produitItem);
        }
        return null;
    }

    @Override
    public List<ProduitItemDto> findAll() {
        List<ProduitItem> produitItems = produitItemDao.findAll();
        produitItems.sort(Comparator.comparing(p -> p.getProduit().getId()));
        return produitItems.stream().map(produitItem -> produitItemMapper.toDto(produitItem)).collect(Collectors.toList());
    }

    @Override
    public List<ProduitItemDto> findByProduitId(Long id) {
        //return findAll().stream().filter(produitItem -> produitItem.getProduit().getId().equals(id)).collect(Collectors.toList());
      return  produitItemDao.findAll().stream().filter(produitItem -> produitItem.getProduit().getId().equals(id)).map(produitItem -> produitItemMapper.toDto(produitItem)).collect(Collectors.toList());
    }

    @Override
    public List<ProduitItemDto> findById(Long id) {
        return findAll().stream().filter(produitItem -> produitItem.getId().equals(id)).collect(Collectors.toList());
    }
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProduitItemService implements IProduitItemService {
    private final ProduitItemMapper produitItemMapper ;
    private final IProduitItemDao produitItemDao ;
    @Override
    public ProduitItemDto save(ProduitItemRegisterDto produitItemRegisterDto) {
        ProduitItem produitItem = produitItemMapper.toEntity(produitItemRegisterDto);
        ProduitItem prduitItemSave  = produitItemDao.save(produitItem);
        return produitItemMapper.toDto(prduitItemSave);
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
        return produitItems.stream().map(produitItem -> produitItemMapper.toDto(produitItem)).collect(Collectors.toList());
    }

    @Override
    public List<ProduitItemDto> findByProduitId(Long id) {
        return findAll().stream().filter(produitItem -> produitItem.getProduit().getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<ProduitItemDto> findById(Long id) {
        return findAll().stream().filter(produitItem -> produitItem.getId().equals(id)).collect(Collectors.toList());
    }
}

package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProduitItemService {
    ProduitItemDto save(ProduitItemRegisterDto produitItemRegisterDto);
    ProduitItemDto update(ProduitItemRegisterDto produitItemRegisterDto, Long id);
    ProduitItemDto delete(Long id);
    ProduitItemDto findById(Long id);
    Page<ProduitItemDto> findAll(Pageable pageable);
    Page<ProduitItemDto> findByProduitId(Long id, Pageable pageable);
}

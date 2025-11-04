package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.ProduitItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("produit/item")
public class PrduitItemController {
    private ProduitItemService produitItemService;
    @PostMapping
    public ResponseEntity<ProduitItemDto> create(@RequestBody ProduitItemRegisterDto produitItemRegisterDto) {
        ProduitItemDto  produitItemDto = produitItemService.save(produitItemRegisterDto);
        return ResponseEntity.ok(produitItemDto);
    }
    @GetMapping
    public ResponseEntity<List<ProduitItemDto>> fingAll(){
        return ResponseEntity.ok(produitItemService.findAll());
    }

}

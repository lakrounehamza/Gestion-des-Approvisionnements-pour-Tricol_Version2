package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseuRregistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/fournisseurs")
public class FournisserController {
    public final IFournisseurService fournisseurService;
    @PostMapping
    public ResponseEntity<FournisseurDto>  createFournisseur(@RequestBody FournisseuRregistreDTO fournisseurDto){
      FournisseurDto f = fournisseurService.save(fournisseurDto);
        return ResponseEntity.of(Optional.ofNullable(f));
    }
    @GetMapping
    public ResponseEntity<List<FournisseurDto>>  getAllFournisseur(){
        return ResponseEntity.ok(fournisseurService.getAll());
    }
}

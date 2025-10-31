package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/mouvements-stock")
public class MouvementsStockController {

    private final IMouvementsStockService mouvementsStockService;

    @PostMapping
    public ResponseEntity<MouvementsStockDto> createMouvement(@Valid @RequestBody MouvementsStockRegisterDto dto) {
        MouvementsStockDto savedMouvement = mouvementsStockService.save(dto);
        return ResponseEntity.ok(savedMouvement);
    }

    @GetMapping
    public ResponseEntity<List<MouvementsStockDto>> getAllMouvements() {
        List<MouvementsStockDto> mouvements = mouvementsStockService.getAll();
        if (mouvements.isEmpty()) {
            throw new NotFoundException("Aucun mouvement de stock trouvé.");
        }
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("{id}")
    public ResponseEntity<MouvementsStockDto> getMouvementById(@PathVariable Long id) {
        MouvementsStockDto mouvement = mouvementsStockService.getById(id);
        if (mouvement == null) {
            throw new NotFoundException("Mouvement introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(mouvement);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MouvementsStockDto> deleteMouvement(@PathVariable Long id) {
        MouvementsStockDto deletedMouvement = mouvementsStockService.delete(id);
        if (deletedMouvement == null) {
            throw new NotFoundException("Impossible de supprimer : mouvement introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedMouvement);
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<MouvementsStockDto>> getMouvementsByProduit(@PathVariable Long produitId) {
        List<MouvementsStockDto> mouvements = mouvementsStockService.getByProduitId(produitId);
        if (mouvements.isEmpty()) {
            throw new NotFoundException("Aucun mouvement trouvé pour le produit avec l'ID : " + produitId);
        }
        return ResponseEntity.ok(mouvements);
    }
}

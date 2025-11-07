package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
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
@RequestMapping("api/v1/mouvements-stock")
public class MouvementsStockController {

    private final IMouvementsStockService mouvementsStockService;

    @PostMapping
    public ResponseEntity<MouvementsStockDto> createMouvement(@Valid @RequestBody MouvementsStockRegisterDto dto) {
        MouvementsStockDto savedMouvement = mouvementsStockService.save(dto);
        return ResponseEntity.ok(savedMouvement);
    }

    @GetMapping
    public ResponseEntity<Page<MouvementsStockDto>> getAllMouvements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<MouvementsStockDto> mouvements = mouvementsStockService.getAll(pageable);
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
    public ResponseEntity<Page<MouvementsStockDto>> getMouvementsByProduit(
            @PathVariable Long produitId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateMouvement") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<MouvementsStockDto> mouvements = mouvementsStockService.getByProduitId(produitId, pageable);
        if (mouvements.isEmpty()) {
            throw new NotFoundException("Aucun mouvement trouvé pour le produit avec l'ID : " + produitId);
        }
        return ResponseEntity.ok(mouvements);
    }
}
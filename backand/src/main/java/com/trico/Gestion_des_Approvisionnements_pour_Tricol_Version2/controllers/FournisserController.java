package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/fournisseurs")
public class FournisserController {

    private final IFournisseurService fournisseurService;

    @PostMapping
    public ResponseEntity<FournisseurDto> createFournisseur(@Valid @RequestBody FournisseurRegistreDTO fournisseurDto) {
        FournisseurDto f = fournisseurService.save(fournisseurDto);
        return ResponseEntity.ok(f);
    }

    @GetMapping
    public ResponseEntity<Page<FournisseurDto>> getAllFournisseur(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<FournisseurDto> fournisseurs = fournisseurService.getAll(pageable);
        if (fournisseurs.isEmpty()) {
            throw new NotFoundException("Aucun fournisseur trouvé.");
        }
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("{id}")
    public ResponseEntity<FournisseurDto> getFournisseurById(@PathVariable Long id) {
        FournisseurDto fournisseur = fournisseurService.getById(id);
        if (fournisseur == null) {
            throw new NotFoundException("Fournisseur introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(fournisseur);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FournisseurDto> deleteFournisseurById(@PathVariable Long id) {
        FournisseurDto deletedFournisseur = fournisseurService.delete(id);
        if (deletedFournisseur == null) {
            throw new NotFoundException("Impossible de supprimer : fournisseur introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedFournisseur);
    }

    @PutMapping("{id}")
    public ResponseEntity<FournisseurDto> updateFournisseurById(@PathVariable Long id, @Valid @RequestBody FournisseurRegistreDTO fournisseurDto) {
        FournisseurDto f = fournisseurService.update(id, fournisseurDto);
        return ResponseEntity.ok(f);
    }
}
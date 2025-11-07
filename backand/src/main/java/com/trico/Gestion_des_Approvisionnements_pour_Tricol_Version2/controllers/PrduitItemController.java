package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.ProduitItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/produits/items/stock")
@AllArgsConstructor
public class PrduitItemController {

    private final ProduitItemService produitItemService;

    @PostMapping
    public ResponseEntity<ProduitItemDto> create(@Valid @RequestBody ProduitItemRegisterDto produitItemRegisterDto) {
        ProduitItemDto produitItemDto = produitItemService.save(produitItemRegisterDto);
        return ResponseEntity.ok(produitItemDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProduitItemDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProduitItemDto> items = produitItemService.findAll(pageable);
        if (items.isEmpty()) {
            throw new NotFoundException("Aucun item de produit trouvé.");
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProduitItemDto> findById(@PathVariable Long id) {
        ProduitItemDto item = produitItemService.findById(id);
        if (item == null) {
            throw new NotFoundException("Item de produit introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProduitItemDto> delete(@PathVariable Long id) {
        ProduitItemDto deletedItem = produitItemService.delete(id);
        if (deletedItem == null) {
            throw new NotFoundException("Impossible de supprimer : item introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedItem);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProduitItemDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProduitItemRegisterDto produitItemRegisterDto) {
        ProduitItemDto updatedItem = produitItemService.update(id, produitItemRegisterDto);
        return ResponseEntity.ok(updatedItem);
    }
}
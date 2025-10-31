package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/commande-items")
public class CommandeItemController {

    private final ICommandeItemService commandeItemService;

    @PostMapping
    public ResponseEntity<CommandeItemDto> createCommandeItem(@Valid @RequestBody CommandeItemRegisterDto dto) {
        CommandeItemDto savedItem = commandeItemService.save(dto);
        return ResponseEntity.ok(savedItem);
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<CommandeItemDto>> getItemsByCommande(@PathVariable Long commandeId) {
        List<CommandeItemDto> items = commandeItemService.getByCommandeId(commandeId);
        if (items.isEmpty()) {
            throw new NotFoundException("Aucun item trouvé pour la commande avec l'ID : " + commandeId);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommandeItemDto> getItemById(@PathVariable Long id) {
        CommandeItemDto item = commandeItemService.getById(id);
        if (item == null) {
            throw new NotFoundException("Item introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommandeItemDto> deleteItem(@PathVariable Long id) {
        CommandeItemDto deletedItem = commandeItemService.delete(id);
        if (deletedItem == null) {
            throw new NotFoundException("Impossible de supprimer : item introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedItem);
    }

    @PutMapping("{id}")
    public ResponseEntity<CommandeItemDto> updateItem(@PathVariable Long id,
                                                      @Valid @RequestBody CommandeItemRegisterDto dto) {
        CommandeItemDto updatedItem = commandeItemService.update(id, dto);
        if (updatedItem == null) {
            throw new NotFoundException("Impossible de mettre à jour : item introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(updatedItem);
    }
}

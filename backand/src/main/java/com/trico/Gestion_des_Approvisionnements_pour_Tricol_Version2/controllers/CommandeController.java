package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeService;
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
@RequestMapping("api/v1/commandes")
public class CommandeController {

    private final ICommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeDto> createCommande(@Valid @RequestBody CommandeRegisterDto commandeDto) {
        CommandeDto savedCommande = commandeService.save(commandeDto);
        return ResponseEntity.ok(savedCommande);
    }

    @GetMapping
    public ResponseEntity<Page<CommandeDto>> getAllCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CommandeDto> commandes = commandeService.getAll(pageable);
        if (commandes.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée.");
        }
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        CommandeDto commande = commandeService.getById(id);
        if (commande == null) {
            throw new NotFoundException("Commande introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(commande);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommandeDto> deleteCommande(@PathVariable Long id) {
        CommandeDto deletedCommande = commandeService.delete(id);
        if (deletedCommande == null) {
            throw new NotFoundException("Impossible de supprimer : commande introuvable avec l'ID : " + id);
        }
        return ResponseEntity.ok(deletedCommande);
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<CommandeDto>> getCommandesByStatut(
            @PathVariable String statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CommandeDto> filteredCommandes = commandeService.getByStatut(statut, pageable);
        if (filteredCommandes.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée avec le statut : " + statut);
        }
        return ResponseEntity.ok(filteredCommandes);
    }
}
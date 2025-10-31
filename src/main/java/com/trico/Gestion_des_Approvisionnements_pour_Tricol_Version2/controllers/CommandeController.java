package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<List<CommandeDto>> getAllCommandes() {
        List<CommandeDto> commandes = commandeService.getAll();
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
    public ResponseEntity<List<CommandeDto>> getCommandesByStatut(@PathVariable String statut) {
        List<CommandeDto> filteredCommandes = commandeService.getByStatut(statut);
        if (filteredCommandes.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée avec le statut : " + statut);
        }
        return ResponseEntity.ok(filteredCommandes);
    }
}

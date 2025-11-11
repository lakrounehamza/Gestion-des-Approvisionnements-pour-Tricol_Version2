package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.CommandeController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.CommandeMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommandeController.class)
class CommandeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private ICommandeService commandeService;

    @MockitoBean
    private CommandeMapper commandeMapper;

    private Commande buildCommande() {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setStatut(StatutCommandeEnum.EN_COURS);
        commande.setMontant(2323.23);
        commande.setDateCommande(new Date());

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        commande.setFournisseur(fournisseur);

        Fournisseur client = new Fournisseur();
        client.setId(2L);
        commande.setClient(client);

        List<CommandeItem> items = new ArrayList<>();
        CommandeItem item1 = new CommandeItem();
        CommandeItem item2 = new CommandeItem();
        item1.setId(1L);
        item2.setId(2L);

        items.add(item1);
        items.add(item2);

        return commande;
    }

    private CommandeDto buildCommandeDto() {
        CommandeDto dto = new CommandeDto();
        dto.setId(1L);
        dto.setStatut(StatutCommandeEnum.EN_COURS);
        dto.setMontant(2323.23);
        dto.setDateCommande(new Date());
        return dto;
    }

    private CommandeRegisterDto buildCommandeRegisterDto() {
        CommandeRegisterDto dto = new CommandeRegisterDto();
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        dto.setFournisseur(fournisseur);

        Fournisseur client = new Fournisseur();
        client.setId(2L);
        dto.setClient(client);

        dto.setStatut(StatutCommandeEnum.EN_COURS);
        dto.setMontant(2323.23);
        dto.setDateCommande(new Date());
        return dto;
    }

    @Test
    void createCommande_ShouldReturnCreatedCommande() throws Exception {
        CommandeRegisterDto registerDto = buildCommandeRegisterDto();
        CommandeDto responseDto = buildCommandeDto();

        when(commandeService.save(any(CommandeRegisterDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(2323.23))
                .andExpect(jsonPath("$.statut").value("EN_COURS"));
    }

    @Test
    void getCommandeById_ShouldReturnCommande_WhenExists() throws Exception {
        CommandeDto dto = buildCommandeDto();

        when(commandeService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/commandes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(2323.23))
                .andExpect(jsonPath("$.statut").value("EN_COURS"));
    }

    @Test
    void getCommandeById_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(commandeService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/commandes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCommandes_ShouldReturnPageOfCommandes() throws Exception {
        List<CommandeDto> commandeList = Arrays.asList(
                buildCommandeDto(),
                buildCommandeDto()
        );

        Page<CommandeDto> commandePage = new PageImpl<>(
                commandeList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                commandeList.size()
        );

        when(commandeService.getAll(any(Pageable.class))).thenReturn(commandePage);

        mockMvc.perform(get("/api/v1/commandes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void getAllCommandes_ShouldThrowNotFoundException_WhenEmpty() throws Exception {
        Page<CommandeDto> emptyPage = Page.empty();

        when(commandeService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/commandes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCommande_ShouldReturnDeletedCommande_WhenExists() throws Exception {
        CommandeDto dto = buildCommandeDto();

        when(commandeService.delete(1L)).thenReturn(dto);

        mockMvc.perform(delete("/api/v1/commandes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montant").value(2323.23));
    }

    @Test
    void deleteCommande_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(commandeService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/commandes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCommandesByStatut_ShouldReturnFilteredCommandes() throws Exception {
        List<CommandeDto> commandeList = Arrays.asList(buildCommandeDto());

        Page<CommandeDto> commandePage = new PageImpl<>(
                commandeList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                commandeList.size()
        );

        when(commandeService.getByStatut(eq("EN_COURS"), any(Pageable.class)))
                .thenReturn(commandePage);

        mockMvc.perform(get("/api/v1/commandes/statut/EN_COURS")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].statut").value("EN_COURS"));
    }

    @Test
    void getCommandesByStatut_ShouldThrowNotFoundException_WhenNoCommandesWithStatut() throws Exception {
        Page<CommandeDto> emptyPage = Page.empty();

        when(commandeService.getByStatut(eq("TERMINEE"), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/commandes/statut/TERMINEE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCommandesByStatut_WithDescendingSort_ShouldReturnSortedCommandes() throws Exception {
        List<CommandeDto> commandeList = Arrays.asList(buildCommandeDto());

        Page<CommandeDto> commandePage = new PageImpl<>(
                commandeList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateCommande")),
                commandeList.size()
        );

        when(commandeService.getByStatut(eq("EN_COURS"), any(Pageable.class)))
                .thenReturn(commandePage);

        mockMvc.perform(get("/api/v1/commandes/statut/EN_COURS")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "dateCommande")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void countByFournisseur_ShouldReturnCount() throws Exception {
        Long expectedCount = 5L;

        when(commandeService.countCommande(1L)).thenReturn(expectedCount);

        mockMvc.perform(get("/api/v1/commandes/count/byfournisser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void countByFournisseur_ShouldReturnZero_WhenNoCommandes() throws Exception {
        when(commandeService.countCommande(999L)).thenReturn(0L);

        mockMvc.perform(get("/api/v1/commandes/count/byfournisser/999"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void createCommande_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        CommandeRegisterDto invalidDto = new CommandeRegisterDto();
        // DTO sans données requises

        mockMvc.perform(post("/api/v1/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
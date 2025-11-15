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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

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
       // commande.setCommandeItems(items);

        return commande;
    }

    @Test
    void createCommande() throws Exception {
        Commande commande = buildCommande();

        CommandeDto dto = commandeMapper.toDto(commande);
        when(commandeService.save(any(CommandeRegisterDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commande)))
                .andExpect(status().isOk());
    }

    @Test
    void getCommande() throws Exception{
        Commande   commande  =  buildCommande();
        Long  id  = commande.getId();
        mockMvc.perform(get("/api/v1/commandes/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

}
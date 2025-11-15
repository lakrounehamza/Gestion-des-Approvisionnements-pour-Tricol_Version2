package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.CommandeItemController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeItemService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommandeItemController.class)
class CommandeItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICommandeItemService commandeItemService;

    private CommandeItemDto buildCommandeItemDto() {
        CommandeItemDto dto = new CommandeItemDto();
        dto.setId(1L);
        dto.setQuantity(10);
        dto.setDate(new Date());
        ProduitDto produitDto = new ProduitDto();
        produitDto.setId(1L);
        dto.setProduit(produitDto);
        return dto;
    }

    private CommandeItemRegisterDto buildCommandeItemRegisterDto() {
        CommandeItemRegisterDto dto = new CommandeItemRegisterDto();
        Commande commande = new Commande();
        Produit produit = new Produit();
        produit.setId(1L);
        commande.setId(2L);
        dto.setCommande(commande);
        dto.setDate(new Date());
        dto.setQuantity(3);
        dto.setProduit(produit);

        return dto;
    }

    @Test
    void createCommandeItem_ShouldReturnCreatedItem() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        CommandeItemDto responseDto = buildCommandeItemDto();

        when(commandeItemService.save(any(CommandeItemRegisterDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/commande-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.produit.id").value(1));
    }

    @Test
    void createCommandeItem_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        CommandeItemRegisterDto invalidDto = new CommandeItemRegisterDto();
        // DTO sans données requises

        mockMvc.perform(post("/api/v1/commande-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCommandeItems_ShouldReturnPageOfItems() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(
                buildCommandeItemDto(),
                buildCommandeItemDto()
        );

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                itemList.size()
        );

        when(commandeItemService.getAll(any(Pageable.class))).thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].quantity").value(10));
    }

    @Test
    void getAllCommandeItems_ShouldThrowNotFoundException_WhenEmpty() throws Exception {
        Page<CommandeItemDto> emptyPage = Page.empty();

        when(commandeItemService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/commande-items"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCommandeItems_WithDescendingSort_ShouldReturnSortedItems() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(buildCommandeItemDto());

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "quantity")),
                itemList.size()
        );

        when(commandeItemService.getAll(any(Pageable.class))).thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items")
                        .param("sortBy", "quantity")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quantity").value(10));
    }

    @Test
    void getItemsByCommande_ShouldReturnFilteredItems() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(buildCommandeItemDto());

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                itemList.size()
        );

        when(commandeItemService.getByCommandeId(eq(1L), any(Pageable.class)))
                .thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items/commande/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].quantity").value(10));
    }

    @Test
    void getItemsByCommande_ShouldThrowNotFoundException_WhenNoItems() throws Exception {
        Page<CommandeItemDto> emptyPage = Page.empty();

        when(commandeItemService.getByCommandeId(eq(999L), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/commande-items/commande/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemsByCommande_WithCustomPagination_ShouldReturnItems() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(buildCommandeItemDto());

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "quantity")),
                itemList.size()
        );

        when(commandeItemService.getByCommandeId(eq(1L), any(Pageable.class)))
                .thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items/commande/1")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sortBy", "quantity")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getItemById_ShouldReturnItem_WhenExists() throws Exception {
        CommandeItemDto dto = buildCommandeItemDto();

        when(commandeItemService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/commande-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.produit.id").value(1));
    }

    @Test
    void getItemById_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(commandeItemService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/commande-items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_ShouldReturnDeletedItem_WhenExists() throws Exception {
        CommandeItemDto dto = buildCommandeItemDto();

        when(commandeItemService.delete(1L)).thenReturn(dto);

        mockMvc.perform(delete("/api/v1/commande-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.produit.id").value(1));
    }

    @Test
    void deleteItem_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(commandeItemService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/commande-items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem_WhenExists() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        registerDto.setQuantity(20);

        CommandeItemDto updatedDto = buildCommandeItemDto();
        updatedDto.setQuantity(20);

        when(commandeItemService.update(eq(1L), any(CommandeItemRegisterDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/commande-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.produit.id").value(1));
    }

    @Test
    void updateItem_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();

        when(commandeItemService.update(eq(999L), any(CommandeItemRegisterDto.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/commande-items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItem_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        CommandeItemRegisterDto invalidDto = new CommandeItemRegisterDto();
        // DTO sans données requises

        mockMvc.perform(put("/api/v1/commande-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_WithPartialData_ShouldUpdateSuccessfully() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        registerDto.setQuantity(15);

        CommandeItemDto updatedDto = buildCommandeItemDto();
        updatedDto.setQuantity(15);

        when(commandeItemService.update(eq(1L), any(CommandeItemRegisterDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/commande-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    void createCommandeItem_WithZeroQuantity_ShouldReturnBadRequest() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        registerDto.setQuantity(0);

        mockMvc.perform(post("/api/v1/commande-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemsByCommande_WithLargePageSize_ShouldReturnItems() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(
                buildCommandeItemDto(),
                buildCommandeItemDto()
        );

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id")),
                itemList.size()
        );

        when(commandeItemService.getByCommandeId(eq(1L), any(Pageable.class)))
                .thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items/commande/1")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(100));
    }

    @Test
    void createCommandeItem_WithValidCommande_ShouldReturnCreatedItem() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        registerDto.setQuantity(5);

        CommandeItemDto responseDto = buildCommandeItemDto();
        responseDto.setQuantity(5);

        when(commandeItemService.save(any(CommandeItemRegisterDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/commande-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.produit").exists())
                .andExpect(jsonPath("$.produit.id").value(1));
    }

    @Test
    void updateItem_WithNewQuantity_ShouldUpdateSuccessfully() throws Exception {
        CommandeItemRegisterDto registerDto = buildCommandeItemRegisterDto();
        registerDto.setQuantity(25);

        CommandeItemDto updatedDto = buildCommandeItemDto();
        updatedDto.setQuantity(25);

        when(commandeItemService.update(eq(1L), any(CommandeItemRegisterDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/commande-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(25));
    }

    @Test
    void getAllCommandeItems_WithDefaultPagination_ShouldReturnFirstPage() throws Exception {
        List<CommandeItemDto> itemList = Arrays.asList(buildCommandeItemDto());

        Page<CommandeItemDto> itemPage = new PageImpl<>(
                itemList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                itemList.size()
        );

        when(commandeItemService.getAll(any(Pageable.class))).thenReturn(itemPage);

        mockMvc.perform(get("/api/v1/commande-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }
}
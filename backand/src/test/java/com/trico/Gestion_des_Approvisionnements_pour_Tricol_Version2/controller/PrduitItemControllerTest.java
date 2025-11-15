package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.PrduitItemController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.ProduitItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrduitItemController.class)
public class PrduitItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProduitItemService produitItemService;

    private ProduitItemDto produitItemDto;
    private ProduitItemRegisterDto registerDto;
    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit Test");

        produitItemDto = ProduitItemDto.builder()
                .id(1L)
                .prix(150.50)
                .quantite(25)
                .build();

        registerDto = ProduitItemRegisterDto.builder()
                .prix(150.50)
                .quantite(25)
                .produit(produit)
                .build();
    }

    @Test
    void create_ShouldReturnCreatedItem() throws Exception {
        when(produitItemService.save(any(ProduitItemRegisterDto.class)))
                .thenReturn(produitItemDto);

        mockMvc.perform(post("/api/v1/produits/items/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.prix", is(150.50)))
                .andExpect(jsonPath("$.quantite", is(25)));

        verify(produitItemService, times(1)).save(any(ProduitItemRegisterDto.class));
    }

    @Test
    void create_WithNullProduit_ShouldAccept() throws Exception {
        registerDto.setProduit(null);

        when(produitItemService.save(any(ProduitItemRegisterDto.class)))
                .thenReturn(produitItemDto);

        mockMvc.perform(post("/api/v1/produits/items/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        verify(produitItemService, times(1)).save(any());
    }

    @Test
    void create_WithNegativePrice_ShouldStillAccept() throws Exception {
        registerDto.setPrix(-10.0);
        ProduitItemDto negativePriceDto = ProduitItemDto.builder()
                .id(1L)
                .prix(-10.0)
                .quantite(25)
                .build();

        when(produitItemService.save(any(ProduitItemRegisterDto.class)))
                .thenReturn(negativePriceDto);

        mockMvc.perform(post("/api/v1/produits/items/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }

    @Test
    void findAll_ShouldReturnPagedItems() throws Exception {
        List<ProduitItemDto> items = Arrays.asList(produitItemDto);
        Page<ProduitItemDto> page = new PageImpl<>(items);

        when(produitItemService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/items/stock")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].prix", is(150.50)))
                .andExpect(jsonPath("$.content[0].quantite", is(25)));

        verify(produitItemService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findAll_WithDescSorting_ShouldReturnPagedItems() throws Exception {
        List<ProduitItemDto> items = Arrays.asList(produitItemDto);
        Page<ProduitItemDto> page = new PageImpl<>(items);

        when(produitItemService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/items/stock")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "prix")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitItemService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findAll_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<ProduitItemDto> emptyPage = new PageImpl<>(Arrays.asList());

        when(produitItemService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/produits/items/stock"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_ShouldReturnItem() throws Exception {
        when(produitItemService.findById(1L)).thenReturn(produitItemDto);

        mockMvc.perform(get("/api/v1/produits/items/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.prix", is(150.50)))
                .andExpect(jsonPath("$.quantite", is(25)));

        verify(produitItemService, times(1)).findById(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(produitItemService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/produits/items/stock/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturnDeletedItem() throws Exception {
        when(produitItemService.delete(1L)).thenReturn(produitItemDto);

        mockMvc.perform(delete("/api/v1/produits/items/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.prix", is(150.50)));

        verify(produitItemService, times(1)).delete(1L);
    }

    @Test
    void delete_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(produitItemService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/produits/items/stock/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_ShouldReturnUpdatedItem() throws Exception {
        ProduitItemDto updatedDto = ProduitItemDto.builder()
                .id(1L)
                .prix(200.00)
                .quantite(30)
                .build();

        when(produitItemService.update(any(ProduitItemRegisterDto.class), anyLong()))
                .thenReturn(updatedDto);

        registerDto.setPrix(200.00);
        registerDto.setQuantite(30);

        mockMvc.perform(put("/api/v1/produits/items/stock/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.prix", is(200.00)))
                .andExpect(jsonPath("$.quantite", is(30)));

        verify(produitItemService, times(1))
                .update(any(ProduitItemRegisterDto.class), eq(1L));
    }

    @Test
    void update_WithInvalidId_ShouldThrowNotFoundException() throws Exception {
        when(produitItemService.update(any(ProduitItemRegisterDto.class), eq(999L)))
                .thenThrow(new NotFoundException("Item introuvable"));

        mockMvc.perform(put("/api/v1/produits/items/stock/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_WithNullProduit_ShouldAccept() throws Exception {
        registerDto.setProduit(null);

        when(produitItemService.update(any(ProduitItemRegisterDto.class), anyLong()))
                .thenReturn(produitItemDto);

        mockMvc.perform(put("/api/v1/produits/items/stock/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        verify(produitItemService, times(1)).update(any(), eq(1L));
    }

    @Test
    void findAll_WithDefaultParameters_ShouldWork() throws Exception {
        List<ProduitItemDto> items = Arrays.asList(produitItemDto);
        Page<ProduitItemDto> page = new PageImpl<>(items);

        when(produitItemService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/items/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitItemService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void create_WithZeroQuantity_ShouldAccept() throws Exception {
        registerDto.setQuantite(0);
        ProduitItemDto zeroQuantityDto = ProduitItemDto.builder()
                .id(1L)
                .prix(150.50)
                .quantite(0)
                .build();

        when(produitItemService.save(any(ProduitItemRegisterDto.class)))
                .thenReturn(zeroQuantityDto);

        mockMvc.perform(post("/api/v1/produits/items/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite", is(0)));
    }

    @Test
    void findAll_WithCustomPageSize_ShouldReturnCorrectPage() throws Exception {
        List<ProduitItemDto> items = Arrays.asList(produitItemDto);
        Page<ProduitItemDto> page = new PageImpl<>(items);

        when(produitItemService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/items/stock")
                        .param("page", "2")
                        .param("size", "20"))
                .andExpect(status().isOk());

        verify(produitItemService, times(1)).findAll(any(Pageable.class));
    }
}
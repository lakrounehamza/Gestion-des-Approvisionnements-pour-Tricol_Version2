package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.ProduitController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitService;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
public class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IProduitService produitService;

    private ProduitDto produitDto;
    private ProduitRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        produitDto = ProduitDto.builder()
                .id(1L)
                .nom("Produit Test")
                .description("Description du produit test")
                .categorie(CategorieEnum.PULL)
                .mouvementsStock(Collections.emptyList())
                .produitItems(Collections.emptyList())
                .build();

        registerDto = ProduitRegisterDto.builder()
                .nom("Produit Test")
                .description("Description du produit test")
                .categorie(CategorieEnum.PULL)
                .commandeItems(Collections.emptyList())
                .mouvementsStock(Collections.emptyList())
                .produits(Collections.emptyList())
                .build();
    }

    @Test
    void createProduit_ShouldReturnCreatedProduit() throws Exception {
        when(produitService.save(any(ProduitRegisterDto.class)))
                .thenReturn(produitDto);

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Produit Test")))
                .andExpect(jsonPath("$.description", is("Description du produit test")))
                .andExpect(jsonPath("$.categorie", is("PULL")));

        verify(produitService, times(1)).save(any(ProduitRegisterDto.class));
    }

    @Test
    void createProduit_WithBlankNom_ShouldReturnBadRequest() throws Exception {
        registerDto.setNom("");

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).save(any());
    }

    @Test
    void createProduit_WithNullCategorie_ShouldReturnBadRequest() throws Exception {
        registerDto.setCategorie(null);

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).save(any());
    }

    @Test
    void createProduit_WithShortNom_ShouldReturnBadRequest() throws Exception {
        registerDto.setNom("A");

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).save(any());
    }

    @Test
    void createProduit_WithLongNom_ShouldReturnBadRequest() throws Exception {
        registerDto.setNom("A".repeat(101));

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).save(any());
    }

    @Test
    void createProduit_WithLongDescription_ShouldReturnBadRequest() throws Exception {
        registerDto.setDescription("A".repeat(256));

        mockMvc.perform(post("/api/v1/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).save(any());
    }

    @Test
    void getAllProduits_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].nom", is("Produit Test")));

        verify(produitService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getAllProduits_WithDescSorting_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getAllProduits_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<ProduitDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(produitService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/produits"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProduitById_ShouldReturnProduit() throws Exception {
        when(produitService.getById(1L)).thenReturn(produitDto);

        mockMvc.perform(get("/api/v1/produits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Produit Test")))
                .andExpect(jsonPath("$.categorie", is("PULL")));

        verify(produitService, times(1)).getById(1L);
    }

    @Test
    void getProduitById_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(produitService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/produits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduit_ShouldReturnDeletedProduit() throws Exception {
        when(produitService.delete(1L)).thenReturn(produitDto);

        mockMvc.perform(delete("/api/v1/produits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Produit Test")));

        verify(produitService, times(1)).delete(1L);
    }

    @Test
    void deleteProduit_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(produitService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/produits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduit_ShouldReturnUpdatedProduit() throws Exception {
        ProduitDto updatedDto = ProduitDto.builder()
                .id(1L)
                .nom("Produit Modifié")
                .description("Description modifiée")
                .categorie(CategorieEnum.PULL)
                .build();

        when(produitService.update(anyLong(), any(ProduitRegisterDto.class)))
                .thenReturn(updatedDto);

        registerDto.setNom("Produit Modifié");
        registerDto.setDescription("Description modifiée");
        registerDto.setCategorie(CategorieEnum.PULL);

        mockMvc.perform(put("/api/v1/produits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Produit Modifié")))
                .andExpect(jsonPath("$.categorie", is("PULL")));

        verify(produitService, times(1))
                .update(eq(1L), any(ProduitRegisterDto.class));
    }

    @Test
    void updateProduit_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        registerDto.setNom("");

        mockMvc.perform(put("/api/v1/produits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(produitService, never()).update(anyLong(), any());
    }

    @Test
    void updateProduit_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(produitService.update(eq(999L), any(ProduitRegisterDto.class)))
                .thenThrow(new NotFoundException("Produit introuvable"));

        mockMvc.perform(put("/api/v1/produits/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProduitsByCategorie_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.getByCategorie(anyString(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/categorie/PULL")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].categorie", is("PULL")));

        verify(produitService, times(1))
                .getByCategorie(eq("PULL"), any(Pageable.class));
    }

    @Test
    void getProduitsByCategorie_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<ProduitDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(produitService.getByCategorie(anyString(), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/produits/categorie/INEXISTANTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProduitsByCategorie_WithCustomSorting_ShouldWork() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.getByCategorie(anyString(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/categorie/PULL")
                        .param("sortBy", "nom")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk());

        verify(produitService, times(1))
                .getByCategorie(anyString(), any(Pageable.class));
    }

    @Test
    void searchProduits_WithNomOnly_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.search(anyString(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("nom", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitService, times(1))
                .search(eq("Test"), eq(null), any(Pageable.class));
    }

    @Test
    void searchProduits_WithReferenceOnly_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.search(any(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("reference", "REF123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitService, times(1))
                .search(eq(null), eq("REF123"), any(Pageable.class));
    }

    @Test
    void searchProduits_WithBothParameters_ShouldReturnPagedProduits() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.search(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("nom", "Test")
                        .param("reference", "REF123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitService, times(1))
                .search(eq("Test"), eq("REF123"), any(Pageable.class));
    }

    @Test
    void searchProduits_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<ProduitDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(produitService.search(any(), any(), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("nom", "Inexistant"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchProduits_WithPaginationAndSorting_ShouldWork() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.search(any(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/produits/search")
                        .param("nom", "Test")
                        .param("page", "1")
                        .param("size", "20")
                        .param("sortBy", "description")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk());

        verify(produitService, times(1))
                .search(anyString(), any(), any(Pageable.class));
    }

    @Test
    void getAllProduits_WithDefaultParameters_ShouldWork() throws Exception {
        List<ProduitDto> produits = Arrays.asList(produitDto);
        Page<ProduitDto> page = new PageImpl<>(produits);

        when(produitService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(produitService, times(1)).getAll(any(Pageable.class));
    }
}
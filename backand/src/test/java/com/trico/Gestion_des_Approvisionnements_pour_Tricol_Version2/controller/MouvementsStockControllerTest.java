package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.MouvementsStockController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.TypeMouvementEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MouvementsStockController.class)
public class MouvementsStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IMouvementsStockService mouvementsStockService;

    private MouvementsStockDto mouvementDto;
    private MouvementsStockRegisterDto registerDto;
    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit Test");

        mouvementDto = MouvementsStockDto.builder()
                .id(1L)
                .datemouvement(new Date())
                .quantity(10)
                .price(100.0)
                .type(TypeMouvementEnum.ENTREE)
                .build();

        registerDto = MouvementsStockRegisterDto.builder()
                .datemouvement(new Date())
                .quantity(10)
                .price(100.0)
                .type(TypeMouvementEnum.ENTREE)
                .produit(produit)
                .build();
    }

    @Test
    void createMouvement_ShouldReturnCreatedMouvement() throws Exception {
        when(mouvementsStockService.save(any(MouvementsStockRegisterDto.class)))
                .thenReturn(mouvementDto);

        mockMvc.perform(post("/api/v1/mouvements-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.price", is(100.0)))
                .andExpect(jsonPath("$.type", is("ENTREE")));

        verify(mouvementsStockService, times(1)).save(any(MouvementsStockRegisterDto.class));
    }

    @Test
    void createMouvement_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        registerDto.setQuantity(-1); // Quantité invalide

        mockMvc.perform(post("/api/v1/mouvements-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        verify(mouvementsStockService, never()).save(any());
    }

    @Test
    void getAllMouvements_ShouldReturnPagedMouvements() throws Exception {
        List<MouvementsStockDto> mouvements = Arrays.asList(mouvementDto);
        Page<MouvementsStockDto> page = new PageImpl<>(mouvements);

        when(mouvementsStockService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/mouvements-stock")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(mouvementsStockService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getAllMouvements_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<MouvementsStockDto> emptyPage = new PageImpl<>(Arrays.asList());

        when(mouvementsStockService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/mouvements-stock"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMouvementById_ShouldReturnMouvement() throws Exception {
        when(mouvementsStockService.getById(1L)).thenReturn(mouvementDto);

        mockMvc.perform(get("/api/v1/mouvements-stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)));

        verify(mouvementsStockService, times(1)).getById(1L);
    }

    @Test
    void getMouvementById_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(mouvementsStockService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/mouvements-stock/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMouvement_ShouldReturnDeletedMouvement() throws Exception {
        when(mouvementsStockService.delete(1L)).thenReturn(mouvementDto);

        mockMvc.perform(delete("/api/v1/mouvements-stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(mouvementsStockService, times(1)).delete(1L);
    }

    @Test
    void deleteMouvement_WhenNotFound_ShouldThrowNotFoundException() throws Exception {
        when(mouvementsStockService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/mouvements-stock/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMouvementsByProduit_ShouldReturnPagedMouvements() throws Exception {
        List<MouvementsStockDto> mouvements = Arrays.asList(mouvementDto);
        Page<MouvementsStockDto> page = new PageImpl<>(mouvements);

        when(mouvementsStockService.getByProduitId(anyLong(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/mouvements-stock/produit/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(mouvementsStockService, times(1))
                .getByProduitId(anyLong(), any(Pageable.class));
    }

    @Test
    void getMouvementsByProduit_WhenEmpty_ShouldThrowNotFoundException() throws Exception {
        Page<MouvementsStockDto> emptyPage = new PageImpl<>(Arrays.asList());

        when(mouvementsStockService.getByProduitId(anyLong(), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/mouvements-stock/produit/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMouvement_WithNullType_ShouldReturnBadRequest() throws Exception {
        registerDto.setType(null);

        mockMvc.perform(post("/api/v1/mouvements-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMouvement_WithNullProduit_ShouldReturnBadRequest() throws Exception {
        registerDto.setProduit(null);

        mockMvc.perform(post("/api/v1/mouvements-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMouvement_WithFutureDate_ShouldReturnBadRequest() throws Exception {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // +1 jour
        registerDto.setDatemouvement(futureDate);

        mockMvc.perform(post("/api/v1/mouvements-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }
}
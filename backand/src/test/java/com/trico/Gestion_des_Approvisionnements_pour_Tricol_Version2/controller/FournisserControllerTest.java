package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.controllers.FournisserController;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FournisserController.class)
public class FournisserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IFournisseurService fournisseurService;

    private FournisseurDto buildFournisseurDto() {
        FournisseurDto dto = new FournisseurDto();
        dto.setId(1L);
        dto.setSociete("youcode");
        dto.setAdresse("123 Rue Test");
        dto.setContact("Ahmed Bennani");
        dto.setEmail("test@fournisseur.com");
        dto.setPhone("0612345678");
        dto.setVille("Casablanca");
        dto.setIce("12345678");
        return dto;
    }

    private FournisseurRegistreDTO buildFournisseurRegistreDTO() {
        FournisseurRegistreDTO dto = new FournisseurRegistreDTO();
        dto.setSociete("youcode");
        dto.setAdresse("123 Rue Test");
        dto.setContact("Ahmed Bennani");
        dto.setEmail("test@fournisseur.com");
        dto.setPhone("0612345678");
        dto.setVille("Casablanca");
        dto.setIce("12345678");
        return dto;
    }

    @Test
    void createFournisseur_ShouldReturnCreatedFournisseur() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        FournisseurDto responseDto = buildFournisseurDto();

        when(fournisseurService.save(any(FournisseurRegistreDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("youcode"))
                .andExpect(jsonPath("$.contact").value("Ahmed Bennani"))
                .andExpect(jsonPath("$.email").value("test@fournisseur.com"))
                .andExpect(jsonPath("$.phone").value("0612345678"))
                .andExpect(jsonPath("$.adresse").value("123 Rue Test"))
                .andExpect(jsonPath("$.ville").value("Casablanca"))
                .andExpect(jsonPath("$.ice").value("12345678"));
    }

    @Test
    void createFournisseur_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        FournisseurRegistreDTO invalidDto = new FournisseurRegistreDTO();
        // DTO sans données requises

        mockMvc.perform(post("/api/v1/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFournisseur_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        registerDto.setEmail("invalid-email");

        mockMvc.perform(post("/api/v1/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllFournisseurs_ShouldReturnPageOfFournisseurs() throws Exception {
        List<FournisseurDto> fournisseurList = Arrays.asList(
                buildFournisseurDto(),
                buildFournisseurDto()
        );

        Page<FournisseurDto> fournisseurPage = new PageImpl<>(
                fournisseurList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                fournisseurList.size()
        );

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(fournisseurPage);

        mockMvc.perform(get("/api/v1/fournisseurs")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].societe").value("youcode"));
    }

    @Test
    void getAllFournisseurs_ShouldThrowNotFoundException_WhenEmpty() throws Exception {
        Page<FournisseurDto> emptyPage = Page.empty();

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/fournisseurs"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllFournisseurs_WithDescendingSort_ShouldReturnSortedFournisseurs() throws Exception {
        List<FournisseurDto> fournisseurList = Arrays.asList(buildFournisseurDto());

        Page<FournisseurDto> fournisseurPage = new PageImpl<>(
                fournisseurList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "societe")),
                fournisseurList.size()
        );

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(fournisseurPage);

        mockMvc.perform(get("/api/v1/fournisseurs")
                        .param("sortBy", "societe")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].societe").exists());
    }

    @Test
    void getAllFournisseurs_WithCustomPageSize_ShouldReturnCorrectSize() throws Exception {
        List<FournisseurDto> fournisseurList = Arrays.asList(
                buildFournisseurDto(),
                buildFournisseurDto(),
                buildFournisseurDto()
        );

        Page<FournisseurDto> fournisseurPage = new PageImpl<>(
                fournisseurList,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id")),
                fournisseurList.size()
        );

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(fournisseurPage);

        mockMvc.perform(get("/api/v1/fournisseurs")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void getFournisseurById_ShouldReturnFournisseur_WhenExists() throws Exception {
        FournisseurDto dto = buildFournisseurDto();

        when(fournisseurService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/fournisseurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("youcode"))
                .andExpect(jsonPath("$.email").value("test@fournisseur.com"))
                .andExpect(jsonPath("$.phone").value("0612345678"));
    }

    @Test
    void getFournisseurById_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(fournisseurService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/fournisseurs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFournisseur_ShouldReturnUpdatedFournisseur_WhenExists() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        registerDto.setSociete("YouCode Updated");
        registerDto.setEmail("updated@fournisseur.com");
        registerDto.setContact("Mohamed Alami");

        FournisseurDto updatedDto = buildFournisseurDto();
        updatedDto.setSociete("YouCode Updated");
        updatedDto.setEmail("updated@fournisseur.com");
        updatedDto.setContact("Mohamed Alami");

        when(fournisseurService.update(eq(1L), any(FournisseurRegistreDTO.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/fournisseurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("YouCode Updated"))
                .andExpect(jsonPath("$.email").value("updated@fournisseur.com"))
                .andExpect(jsonPath("$.contact").value("Mohamed Alami"));
    }

    @Test
    void updateFournisseur_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();

        when(fournisseurService.update(eq(999L), any(FournisseurRegistreDTO.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/fournisseurs/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFournisseur_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        FournisseurRegistreDTO invalidDto = new FournisseurRegistreDTO();
        // DTO sans données requises

        mockMvc.perform(put("/api/v1/fournisseurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFournisseur_WithPartialData_ShouldUpdateSuccessfully() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        registerDto.setPhone("0698765432");

        FournisseurDto updatedDto = buildFournisseurDto();
        updatedDto.setPhone("0698765432");

        when(fournisseurService.update(eq(1L), any(FournisseurRegistreDTO.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/fournisseurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("0698765432"));
    }

    @Test
    void deleteFournisseur_ShouldReturnDeletedFournisseur_WhenExists() throws Exception {
        FournisseurDto dto = buildFournisseurDto();

        when(fournisseurService.delete(1L)).thenReturn(dto);

        mockMvc.perform(delete("/api/v1/fournisseurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("youcode"))
                .andExpect(jsonPath("$.email").value("test@fournisseur.com"));
    }

    @Test
    void deleteFournisseur_ShouldThrowNotFoundException_WhenNotExists() throws Exception {
        when(fournisseurService.delete(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/fournisseurs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createFournisseur_WithCompleteData_ShouldReturnCreatedFournisseur() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        FournisseurDto responseDto = buildFournisseurDto();

        when(fournisseurService.save(any(FournisseurRegistreDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.societe").value("youcode"))
                .andExpect(jsonPath("$.ville").value("Casablanca"))
                .andExpect(jsonPath("$.ice").value("12345678"));
    }

    @Test
    void updateFournisseur_WithNewAddress_ShouldUpdateSuccessfully() throws Exception {
        FournisseurRegistreDTO registerDto = buildFournisseurRegistreDTO();
        registerDto.setAdresse("456 Nouvelle Rue");
        registerDto.setVille("Rabat");

        FournisseurDto updatedDto = buildFournisseurDto();
        updatedDto.setAdresse("456 Nouvelle Rue");
        updatedDto.setVille("Rabat");

        when(fournisseurService.update(eq(1L), any(FournisseurRegistreDTO.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/fournisseurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adresse").value("456 Nouvelle Rue"))
                .andExpect(jsonPath("$.ville").value("Rabat"));
    }

    @Test
    void getAllFournisseurs_WithDefaultPagination_ShouldReturnFirstPage() throws Exception {
        List<FournisseurDto> fournisseurList = Arrays.asList(buildFournisseurDto());

        Page<FournisseurDto> fournisseurPage = new PageImpl<>(
                fournisseurList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                fournisseurList.size()
        );

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(fournisseurPage);

        mockMvc.perform(get("/api/v1/fournisseurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }

    @Test
    void getAllFournisseurs_SortByMultipleFields_ShouldReturnSortedList() throws Exception {
        List<FournisseurDto> fournisseurList = Arrays.asList(
                buildFournisseurDto(),
                buildFournisseurDto()
        );

        Page<FournisseurDto> fournisseurPage = new PageImpl<>(
                fournisseurList,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "ville")),
                fournisseurList.size()
        );

        when(fournisseurService.getAll(any(Pageable.class))).thenReturn(fournisseurPage);

        mockMvc.perform(get("/api/v1/fournisseurs")
                        .param("sortBy", "ville")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].ville").value("Casablanca"));
    }

    @Test
    void createFournisseur_WithMinimalRequiredData_ShouldReturnCreatedFournisseur() throws Exception {
        FournisseurRegistreDTO registerDto = new FournisseurRegistreDTO();
        registerDto.setSociete("Fournisseur Minimal");
        registerDto.setAdresse("123 Rue Minimal");
        registerDto.setContact("Contact Minimal");
        registerDto.setEmail("minimal@fournisseur.com");
        registerDto.setPhone("0612345678");
        registerDto.setVille("Casablanca");
        registerDto.setIce("12345678");

        FournisseurDto responseDto = new FournisseurDto();
        responseDto.setId(1L);
        responseDto.setSociete("Fournisseur Minimal");
        responseDto.setAdresse("123 Rue Minimal");
        responseDto.setContact("Contact Minimal");
        responseDto.setEmail("minimal@fournisseur.com");
        responseDto.setPhone("0612345678");
        responseDto.setVille("Casablanca");
        responseDto.setIce("12345678");

        when(fournisseurService.save(any(FournisseurRegistreDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.societe").value("Fournisseur Minimal"))
                .andExpect(jsonPath("$.email").value("minimal@fournisseur.com"));
    }
}
package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.ProduitItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.ProduitItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProduitItemServiceTest {

    @Mock
    private ProduitItemMapper produitItemMapper;

    @Mock
    private IProduitItemDao produitItemDao;

    @Mock
    private IProduitDao produitDao;

    @Mock
    private ProduitMapper produitMapper;

    @InjectMocks
    private ProduitItemService produitItemService;

    private Produit produit;
    private ProduitItem produitItem;
    private ProduitItemDto produitItemDto;
    private ProduitItemRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit Test");

        produitItem = new ProduitItem();
        produitItem.setId(1L);
        produitItem.setPrix(100.0);
        produitItem.setQuantite(10);
        produitItem.setProduit(produit);

        produitItemDto = new ProduitItemDto();
        produitItemDto.setId(1L);
        produitItemDto.setPrix(100.0);
        produitItemDto.setQuantite(10);

        registerDto = new ProduitItemRegisterDto();
        registerDto.setPrix(100.0);
        registerDto.setQuantite(10);
        registerDto.setProduit(produit);
    }

    @Test
    void save_ShouldReturnSavedItem() {
        when(produitDao.findById(1L)).thenReturn(Optional.of(produit));
        when(produitItemMapper.toEntity(registerDto)).thenReturn(produitItem);
        when(produitItemDao.save(produitItem)).thenReturn(produitItem);
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);

        ProduitItemDto result = produitItemService.save(registerDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getPrix());
        verify(produitItemDao, times(1)).save(produitItem);
    }

    @Test
    void save_WithNullProduit_ShouldThrowException() {
        registerDto.setProduit(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produitItemService.save(registerDto);
        });
        assertEquals("L'ID du produit ne peut pas être null", exception.getMessage());
    }

    @Test
    void update_ShouldReturnUpdatedItem() {
        when(produitItemDao.findById(1L)).thenReturn(Optional.of(produitItem));
        when(produitDao.findById(1L)).thenReturn(Optional.of(produit));
        when(produitItemDao.save(produitItem)).thenReturn(produitItem);
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);
        registerDto.setPrix(200.0);
        registerDto.setQuantite(20);
        registerDto.setProduit(produit);

        ProduitItemDto result = produitItemService.update(registerDto, 1L);

        assertNotNull(result);
        verify(produitItemDao).save(produitItem);
    }


    @Test
    void update_WithInvalidId_ShouldThrowNotFoundException() {
        when(produitItemDao.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produitItemService.update(registerDto, 999L);
        });

        assertEquals("ProduitItem introuvable avec l'ID : 999", exception.getMessage());
    }

    @Test
    void delete_ShouldReturnDeletedItem() {
        when(produitItemDao.findById(1L)).thenReturn(Optional.of(produitItem));
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);

        ProduitItemDto result = produitItemService.delete(1L);

        assertNotNull(result);
        verify(produitItemDao, times(1)).delete(produitItem);
    }

    @Test
    void delete_WithInvalidId_ShouldThrowNotFoundException() {
        when(produitItemDao.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produitItemService.delete(999L);
        });

        assertEquals("ProduitItem introuvable avec l'ID : 999", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnItem() {
        when(produitItemDao.findById(1L)).thenReturn(Optional.of(produitItem));
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);

        ProduitItemDto result = produitItemService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowNotFoundException() {
        when(produitItemDao.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produitItemService.findById(999L);
        });

        assertEquals("ProduitItem introuvable avec l'ID : 999", exception.getMessage());
    }

    @Test
    void findAll_ShouldReturnPagedItems() {
        Page<ProduitItem> page = new PageImpl<>(Arrays.asList(produitItem));
        when(produitItemDao.findAll(any(Pageable.class))).thenReturn(page);
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);

        Page<ProduitItemDto> result = produitItemService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findAll_WhenEmpty_ShouldThrowNotFoundException() {
        Page<ProduitItem> emptyPage = new PageImpl<>(Arrays.asList());
        when(produitItemDao.findAll(any(Pageable.class))).thenReturn(emptyPage);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produitItemService.findAll(Pageable.unpaged());
        });

        assertEquals("Aucun ProduitItem trouvé.", exception.getMessage());
    }

    @Test
    void findByProduitId_ShouldReturnPagedItems() {
        Page<ProduitItem> page = new PageImpl<>(Arrays.asList(produitItem));
        when(produitItemDao.findByProduitId(1L, Pageable.unpaged())).thenReturn(page);
        when(produitItemMapper.toDto(produitItem)).thenReturn(produitItemDto);

        Page<ProduitItemDto> result = produitItemService.findByProduitId(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findByProduitId_WhenEmpty_ShouldThrowNotFoundException() {
        Page<ProduitItem> emptyPage = new PageImpl<>(Arrays.asList());
        when(produitItemDao.findByProduitId(1L, Pageable.unpaged())).thenReturn(emptyPage);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produitItemService.findByProduitId(1L, Pageable.unpaged());
        });

        assertEquals("Aucun ProduitItem trouvé pour le produit avec l'ID : 1", exception.getMessage());
    }
}

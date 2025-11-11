package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IProduitDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.ProduitRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.ProduitMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.ProduitServiceImpl;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IProduitItemService;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class ProduitServiceImplTest {

    @InjectMocks
    private ProduitServiceImpl produitService;

    @Mock
    private ProduitMapper produitMapper;

    @Mock
    private IProduitDao produitDao;

    @Mock
    private IProduitItemService produitItemService;

    @Mock
    private IMouvementsStockService mouvementsStockService;

    @Mock
    private Object produitItemMapper; // inutile ici mais injecté par constructeur

    private Produit produit;
    private ProduitDto produitDto;
    private ProduitRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit A");
        produit.setDescription("Description A");
        produit.setCategorie(CategorieEnum.PULL);

        produitDto = new ProduitDto();
        produitDto.setId(1L);
        produitDto.setNom("Produit A");
        produitDto.setDescription("Description A");
        produitDto.setCategorie(CategorieEnum.PULL);

        registerDto = new ProduitRegisterDto();
        registerDto.setNom("Produit A");
        registerDto.setDescription("Description A");
        registerDto.setCategorie(CategorieEnum.PULL);
    }

    @Test
    void save_ShouldReturnProduitDto() {
        when(produitMapper.toEntity(registerDto)).thenReturn(produit);
        when(produitDao.save(produit)).thenReturn(produit);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        ProduitDto result = produitService.save(registerDto);

        assertNotNull(result);
        assertEquals("Produit A", result.getNom());
        verify(produitDao).save(produit);
    }

    @Test
    void update_ShouldReturnUpdatedProduit() {
        when(produitDao.findById(1L)).thenReturn(Optional.of(produit));
        when(produitDao.save(produit)).thenReturn(produit);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        registerDto.setNom("Produit B");
        ProduitDto result = produitService.update(1L, registerDto);

        assertNotNull(result);
        assertEquals("Produit A", result.getNom()); // mapping DTO unchanged
        verify(produitDao).save(produit);
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenProduitDoesNotExist() {
        when(produitDao.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> produitService.update(1L, registerDto));

        assertEquals("Le produit avec l'ID 1 n'existe pas pour la modification.", exception.getMessage());
    }

    @Test
    void delete_ShouldReturnDeletedProduit() {
        when(produitDao.findById(1L)).thenReturn(Optional.of(produit));
        doNothing().when(produitDao).delete(produit);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        ProduitDto result = produitService.delete(1L);

        assertNotNull(result);
        verify(produitDao).delete(produit);
    }

    @Test
    void getById_ShouldReturnProduit() {
        when(produitDao.findById(1L)).thenReturn(Optional.of(produit));
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        ProduitDto result = produitService.getById(1L);

        assertNotNull(result);
        assertEquals("Produit A", result.getNom());
    }

    @Test
    void getById_ShouldThrowNotFound_WhenProduitDoesNotExist() {
        when(produitDao.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> produitService.getById(1L));

        assertEquals("Le produit avec l'ID 1 n'existe pas.", exception.getMessage());
    }

    @Test
    void getAll_ShouldReturnPageOfProduit() {
        Page<Produit> page = new PageImpl<>(List.of(produit));
        when(produitDao.findAll(Pageable.unpaged())).thenReturn(page);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        Page<ProduitDto> result = produitService.getAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByCategorie_ShouldReturnPageOfProduit() {
        Page<Produit> page = new PageImpl<>(List.of(produit));
        when(produitDao.findByCategorie(CategorieEnum.PULL, Pageable.unpaged())).thenReturn(page);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        Page<ProduitDto> result = produitService.getByCategorie("PULL", Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByCategorie_ShouldThrowNotFound_WhenInvalidCategorie() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> produitService.getByCategorie("INVALID", Pageable.unpaged()));

        assertTrue(exception.getMessage().contains("Catégorie invalide"));
    }

    @Test
    void search_ShouldReturnPage_WhenMatchingNom() {
        Page<Produit> page = new PageImpl<>(List.of(produit));
        when(produitDao.findByNomContainingIgnoreCase("Produit", Pageable.unpaged())).thenReturn(page);
        when(produitMapper.toDto(produit)).thenReturn(produitDto);

        Page<ProduitDto> result = produitService.search("Produit", null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }
}

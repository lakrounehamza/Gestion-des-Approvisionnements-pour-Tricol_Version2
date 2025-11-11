package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;


import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IMouvementsStockDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.ProduitDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.TypeMouvementEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.MouvementsStockMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.MouvementsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class MouvementsServiceImplTest {

    @Mock
    private IMouvementsStockDao mouvementsStockDao;

    @Mock
    private MouvementsStockMapper mouvementsStockMapper;

    @InjectMocks
    private MouvementsServiceImpl service;

    private MouvementsStock mouvement;
    private MouvementsStockDto mouvementDto;
    private MouvementsStockRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerDto = new MouvementsStockRegisterDto();
        registerDto.setDatemouvement(new Date());
        registerDto.setQuantity(10);
        registerDto.setPrice(50.0);
        Produit  produit = new Produit();
        produit.setId(1L);
        registerDto.setProduit(produit);
        registerDto.setType(TypeMouvementEnum.ENTREE);

        mouvement = new MouvementsStock();
        mouvement.setId(1L);
        mouvement.setDatemouvement(registerDto.getDatemouvement());
        mouvement.setQuantity(10);
        mouvement.setPrice(50.0);
        mouvement.setProduit(produit);
        mouvement.setType(TypeMouvementEnum.ENTREE);

        mouvementDto = new MouvementsStockDto();
        mouvementDto.setId(1L);
        mouvementDto.setDatemouvement(registerDto.getDatemouvement());
        mouvementDto.setQuantity(10);
        mouvementDto.setPrice(50.0);
        mouvementDto.setType(TypeMouvementEnum.ENTREE);
    }
    @Test
    void testSave() {
        when(mouvementsStockMapper.toEntity(registerDto)).thenReturn(mouvement);
        when(mouvementsStockDao.save(mouvement)).thenReturn(mouvement);
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        MouvementsStockDto result = service.save(registerDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(mouvementsStockDao).save(mouvement);
    }

    @Test
    void testUpdate_WhenFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.of(mouvement));
        when(mouvementsStockDao.save(mouvement)).thenReturn(mouvement);
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        MouvementsStockDto result = service.update(1L, registerDto);

        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.update(1L, registerDto));
    }

    @Test
    void testDelete_WhenFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.of(mouvement));
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        MouvementsStockDto result = service.delete(1L);

        assertEquals(1L, result.getId());
        verify(mouvementsStockDao).delete(mouvement);
    }

    @Test
    void testDelete_WhenNotFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.delete(1L));
    }
    @Test
    void testGetById_WhenFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.of(mouvement));
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        MouvementsStockDto result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_WhenNotFound() {
        when(mouvementsStockDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void testGetAll_WhenNotEmpty() {
        Page<MouvementsStock> page = new PageImpl<>(List.of(mouvement));
        when(mouvementsStockDao.findAll(any(PageRequest.class))).thenReturn(page);
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        Page<MouvementsStockDto> result = service.getAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAll_WhenEmpty() {
        Page<MouvementsStock> emptyPage = new PageImpl<>(List.of());
        when(mouvementsStockDao.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class,
                () -> service.getAll(PageRequest.of(0, 10)));
    }

    @Test
    void testGetByProduitId_WhenFound() {
        Page<MouvementsStock> page = new PageImpl<>(List.of(mouvement));
        when(mouvementsStockDao.findByProduitId(eq(5L), any(PageRequest.class)))
                .thenReturn(page);
        when(mouvementsStockMapper.toDto(mouvement)).thenReturn(mouvementDto);

        Page<MouvementsStockDto> result =
                service.getByProduitId(5L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetByProduitId_WhenEmpty() {
        Page<MouvementsStock> emptyPage = new PageImpl<>(List.of());
        when(mouvementsStockDao.findByProduitId(eq(5L), any(PageRequest.class)))
                .thenReturn(emptyPage);

        assertThrows(NotFoundException.class,
                () -> service.getByProduitId(5L, PageRequest.of(0, 10)));
    }
}

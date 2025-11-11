package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;


import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IFournisseurDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.FournisseurMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.FournisseurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class FournisseurServiceImplTest {

    @Mock
    private FournisseurMapper fournisseurMapper;

    @Mock
    private IFournisseurDao fournisseurDao;

    @InjectMocks
    private FournisseurServiceImpl service;

    private Fournisseur fournisseur;
    private FournisseurDto fournisseurDto;
    private FournisseurRegistreDTO registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerDto = new FournisseurRegistreDTO();
        registerDto.setIce("12345");
        registerDto.setEmail("test@gmail.com");
        registerDto.setPhone("0600000000");
        registerDto.setAdresse("Adresse test");

        fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        fournisseur.setIce("12345");
        fournisseur.setEmail("test@gmail.com");
        fournisseur.setPhone("0600000000");
        fournisseur.setAdresse("Adresse test");

        fournisseurDto = new FournisseurDto();
        fournisseurDto.setId(1L);
        fournisseurDto.setIce("12345");
        fournisseurDto.setEmail("test@gmail.com");
        fournisseurDto.setPhone("0600000000");
        fournisseurDto.setAdresse("Adresse test");
    }

    @Test
    void testSave() {
        when(fournisseurMapper.toEntity(registerDto)).thenReturn(fournisseur);
        when(fournisseurDao.save(fournisseur)).thenReturn(fournisseur);
        when(fournisseurMapper.toDto(fournisseur)).thenReturn(fournisseurDto);

        FournisseurDto result = service.save(registerDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(fournisseurDao).save(fournisseur);
    }

    @Test
    void testUpdate_WhenFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(fournisseurDao.save(fournisseur)).thenReturn(fournisseur);
        when(fournisseurMapper.toDto(fournisseur)).thenReturn(fournisseurDto);

        FournisseurDto result = service.update(1L, registerDto);

        assertEquals(1L, result.getId());
        verify(fournisseurDao).save(fournisseur);
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, registerDto));
    }

    @Test
    void testDelete_WhenFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(fournisseurMapper.toDto(fournisseur)).thenReturn(fournisseurDto);

        FournisseurDto result = service.delete(1L);

        assertEquals(1L, result.getId());
        verify(fournisseurDao).delete(fournisseur);
    }

    @Test
    void testDelete_WhenNotFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void testGetById_WhenFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(fournisseurMapper.toDto(fournisseur)).thenReturn(fournisseurDto);

        FournisseurDto result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_WhenNotFound() {
        when(fournisseurDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void testGetAll_WhenNotEmpty() {
        Page<Fournisseur> page = new PageImpl<>(List.of(fournisseur));
        when(fournisseurDao.findAll(any(PageRequest.class))).thenReturn(page);
        when(fournisseurMapper.toDto(fournisseur)).thenReturn(fournisseurDto);

        Page<FournisseurDto> result = service.getAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAll_WhenEmpty() {
        Page<Fournisseur> emptyPage = new PageImpl<>(List.of());
        when(fournisseurDao.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> service.getAll(PageRequest.of(0, 10)));
    }
}

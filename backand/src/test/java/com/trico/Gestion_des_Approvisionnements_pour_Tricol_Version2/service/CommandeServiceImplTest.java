package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;


import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.ICommandeDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.StatutCommandeEnum;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.CommandeMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.CommandeServiceImpl;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeService;
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

class CommandeServiceImplTest {

    @Mock
    private ICommandeDao commandeDao;

    @Mock
    private CommandeMapper commandeMapper;

    @InjectMocks
    private CommandeServiceImpl service;

    private Commande commande;
    private CommandeDto commandeDto;
    private CommandeRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerDto = new CommandeRegisterDto();
        Fournisseur client = new Fournisseur();
        client.setId(1L);
        registerDto.setClient(client);
        registerDto.setStatut(StatutCommandeEnum.EN_COURS);
        registerDto.setMontant(500.0);
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(2L);
        registerDto.setFournisseur(fournisseur);
        registerDto.setDateCommande(new Date());

        commande = new Commande();
        commande.setId(1L);
        commande.setClient(client);
        commande.setStatut(StatutCommandeEnum.EN_COURS);
        commande.setMontant(500.0);
        commande.setFournisseur(fournisseur);
        commande.setDateCommande(registerDto.getDateCommande());

        FournisseurDto clientDto = new FournisseurDto();
        clientDto.setId(1L);
        FournisseurDto fournniseurDto = new FournisseurDto();
        commandeDto = new CommandeDto();
        commandeDto.setId(1L);
        commandeDto.setClient(clientDto);
        commandeDto.setStatut(StatutCommandeEnum.EN_COURS);
        commandeDto.setMontant(500.0);
        commandeDto.setFournisseur(fournniseurDto);
        commandeDto.setDateCommande(registerDto.getDateCommande());
    }

    @Test
    void testSave() {
        when(commandeMapper.toEntity(registerDto)).thenReturn(commande);
        when(commandeDao.save(commande)).thenReturn(commande);
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        CommandeDto result = service.save(registerDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(commandeDao).save(commande);
    }

    @Test
    void testUpdate_WhenFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeDao.save(any(Commande.class))).thenReturn(commande);
        when(commandeMapper.toDto(any(Commande.class))).thenReturn(commandeDto);

        CommandeDto result = service.update(1L, registerDto);

        assertEquals(1L, result.getId());
        verify(commandeDao).save(commande);
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.update(1L, registerDto));
    }

    @Test
    void testDelete_WhenFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        CommandeDto result = service.delete(1L);

        assertEquals(1L, result.getId());
        verify(commandeDao).deleteById(1L);
    }

    @Test
    void testDelete_WhenNotFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }


    @Test
    void testGetById_WhenFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        CommandeDto result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_WhenNotFound() {
        when(commandeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void testGetAll_WhenNotEmpty() {
        Page<Commande> page = new PageImpl<>(List.of(commande));
        when(commandeDao.findAll(any(PageRequest.class))).thenReturn(page);
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        Page<CommandeDto> result =
                service.getAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAll_WhenEmpty() {
        Page<Commande> emptyPage = new PageImpl<>(List.of());
        when(commandeDao.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class,
                () -> service.getAll(PageRequest.of(0, 10)));
    }


    @Test
    void testGetByStatut_WhenFound() {
        Page<Commande> page = new PageImpl<>(List.of(commande));
        when(commandeDao.findByStatut(eq("EN_COURS"), any(PageRequest.class)))
                .thenReturn(page);
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        Page<CommandeDto> result =
                service.getByStatut("EN_COURS", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetByStatut_WhenEmpty() {
        Page<Commande> emptyPage = new PageImpl<>(List.of());
        when(commandeDao.findByStatut(eq("EN_COURS"), any(PageRequest.class)))
                .thenReturn(emptyPage);

        assertThrows(NotFoundException.class,
                () -> service.getByStatut("EN_COURS", PageRequest.of(0, 10)));
    }

}

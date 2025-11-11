package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.ICommandeItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.CommandeItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl.CommandeItemServiceImpl;
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

class CommandeItemServiceImplTest {

    @Mock
    private ICommandeItemDao commandeItemDao;

    @Mock
    private CommandeItemMapper commandeItemMapper;

    @InjectMocks
    private CommandeItemServiceImpl service;

    private CommandeItem item;
    private CommandeItemDto itemDto;
    private CommandeItemRegisterDto registerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerDto = new CommandeItemRegisterDto();
        registerDto.setQuantity(5);
        registerDto.setDate(new Date());
        registerDto.setCommande(null);
        registerDto.setProduit(null);

        item = new CommandeItem();
        item.setId(1L);
        item.setQuantity(5);
        item.setDate(registerDto.getDate());

        itemDto = new CommandeItemDto();
        itemDto.setId(1L);
        itemDto.setQuantity(5);
        itemDto.setDate(item.getDate());
    }

    @Test
    void testSave() {
        when(commandeItemMapper.toEntity(registerDto)).thenReturn(item);
        when(commandeItemDao.save(item)).thenReturn(item);
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        CommandeItemDto result = service.save(registerDto);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        verify(commandeItemDao, times(1)).save(item);
    }

    @Test
    void testUpdate_WhenItemExists() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.of(item));
        when(commandeItemDao.save(item)).thenReturn(item);
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        CommandeItemDto result = service.update(1L, registerDto);

        assertEquals(itemDto.getId(), result.getId());
        verify(commandeItemDao).save(item);
    }

    @Test
    void testUpdate_WhenItemNotFound() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, registerDto));
    }

    @Test
    void testDelete_WhenItemExists() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.of(item));
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        CommandeItemDto result = service.delete(1L);

        assertEquals(itemDto.getId(), result.getId());
        verify(commandeItemDao).delete(item);
    }

    @Test
    void testDelete_WhenItemNotFound() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void testGetById_WhenItemExists() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.of(item));
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        CommandeItemDto result = service.getById(1L);

        assertEquals(itemDto.getId(), result.getId());
    }

    @Test
    void testGetById_WhenItemNotFound() {
        when(commandeItemDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void testGetAll_WhenItemsExist() {
        Page<CommandeItem> page = new PageImpl<>(List.of(item));
        when(commandeItemDao.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        Page<CommandeItemDto> result = service.getAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAll_WhenNoItems() {
        Page<CommandeItem> emptyPage = new PageImpl<>(List.of());
        when(commandeItemDao.findAll(PageRequest.of(0, 10))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> service.getAll(PageRequest.of(0, 10)));
    }

    @Test
    void testGetByCommandeId_WhenItemsExist() {
        Page<CommandeItem> page = new PageImpl<>(List.of(item));
        when(commandeItemDao.findByCommandeId(1L, PageRequest.of(0, 10))).thenReturn(page);
        when(commandeItemMapper.toDto(item)).thenReturn(itemDto);

        Page<CommandeItemDto> result = service.getByCommandeId(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetByCommandeId_WhenNoItems() {
        Page<CommandeItem> emptyPage = new PageImpl<>(List.of());
        when(commandeItemDao.findByCommandeId(1L, PageRequest.of(0, 10))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> service.getByCommandeId(1L, PageRequest.of(0, 10)));
    }
}

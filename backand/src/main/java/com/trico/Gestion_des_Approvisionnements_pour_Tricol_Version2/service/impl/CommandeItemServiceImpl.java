package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.ICommandeItemDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.CommandeItem;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.CommandeItemMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeItemService;
 import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeItemServiceImpl implements ICommandeItemService {

    private final ICommandeItemDao commandeItemDao;
    private final CommandeItemMapper commandeItemMapper;

    @Override
    public CommandeItemDto save(CommandeItemRegisterDto dto) {
        CommandeItem commandeItem = commandeItemMapper.toEntity(dto);
        CommandeItem saved = commandeItemDao.save(commandeItem);
        return commandeItemMapper.toDto(saved);
    }

    @Override
    public CommandeItemDto update(Long id, CommandeItemRegisterDto dto) {
        CommandeItem existingItem = commandeItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("L’élément de commande avec l’ID " + id + " n’existe pas pour la modification."));

        existingItem.setCommande(dto.getCommande());
        existingItem.setQuantity(dto.getQuantity());
        existingItem.setDate(dto.getDate());
        existingItem.setProduit(dto.getProduit());

        CommandeItem updated = commandeItemDao.save(existingItem);
        return commandeItemMapper.toDto(updated);
    }

    @Override
    public CommandeItemDto delete(Long id) {
        CommandeItem commandeItem = commandeItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("L’élément de commande avec l’ID " + id + " n’existe pas pour la suppression."));
        commandeItemDao.delete(commandeItem);
        return commandeItemMapper.toDto(commandeItem);
    }

    @Override
    public CommandeItemDto getById(Long id) {
        CommandeItem commandeItem = commandeItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException("L’élément de commande avec l’ID " + id + " n’existe pas."));
        return commandeItemMapper.toDto(commandeItem);
    }

    @Override
    public List<CommandeItemDto> getAll() {
        List<CommandeItem> items = commandeItemDao.findAll();
        if (items.isEmpty()) {
            throw new NotFoundException("Aucun élément de commande trouvé.");
        }
        return items.stream()
                .map(commandeItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeItemDto> getByCommandeId(Long id) {
        return commandeItemDao.findAll().
                stream().
                filter(commandeItemDto -> commandeItemDto.getCommande().getId().equals(id)).map(commandeItemMapper::toDto).
                collect(Collectors.toList());
    }
}

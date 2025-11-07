package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.ICommandeDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Commande;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.CommandeMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.ICommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements ICommandeService {

    private final ICommandeDao commandeDao;
    private final CommandeMapper commandeMapper;

    @Override
    public CommandeDto save(CommandeRegisterDto commandeDto) {
        Commande commande = commandeMapper.toEntity(commandeDto);
        Commande savedCommande = commandeDao.save(commande);
        return commandeMapper.toDto(savedCommande);
    }

    @Override
    public CommandeDto update(Long id, CommandeRegisterDto commandeDto) {
        Optional<Commande> optionalCommande = commandeDao.findById(id);
        if (optionalCommande.isEmpty()) {
            throw new RuntimeException("Commande non trouvée avec l'id : " + id);
        }

        Commande existing = optionalCommande.get();

        existing.setClient(commandeDto.getClient());
        existing.setStatut(commandeDto.getStatut());
        existing.setMontant(commandeDto.getMontant());
        existing.setFournisseur(commandeDto.getFournisseur());
        //existing.setCommandeItems(commandeDto.getCommandeItems());
        existing.setDateCommande(commandeDto.getDateCommande());

        Commande updated = commandeDao.save(existing);
        return commandeMapper.toDto(updated);
    }

    @Override
    public CommandeDto delete(Long id) {
        Optional<Commande> optionalCommande = commandeDao.findById(id);
        if (optionalCommande.isEmpty()) {
            throw new RuntimeException("Commande non trouvée avec l'id : " + id);
        }

        Commande commande = optionalCommande.get();
        commandeDao.deleteById(id);
        return commandeMapper.toDto(commande);
    }

    @Override
    public CommandeDto getById(Long id) {
        return commandeDao.findById(id)
                .map(commandeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'id : " + id));
    }

    @Override
    public List<CommandeDto> getAll() {
        return commandeDao.findAll()
                .stream()
                .map(commandeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommandeDto> getByStatut(String status) {
        return commandeDao.findAll().
                stream().
                filter(commande -> commande.getStatut().equals(status)).
                map(commande -> commandeMapper.toDto(commande)).
                collect(Collectors.toList());
    }
}

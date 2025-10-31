package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;

import java.util.List;

public interface ICommandeService {
    CommandeDto save(CommandeRegisterDto commandeDto);
    CommandeDto update(Long id ,CommandeRegisterDto commandeDto);
    CommandeDto delete(Long id);
    CommandeDto getById(Long id);
    List<CommandeDto> getAll();
    List<CommandeDto> getByStatut(String status);
}

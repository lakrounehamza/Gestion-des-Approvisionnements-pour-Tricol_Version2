package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeItemDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeItemRegisterDto;

import java.util.List;

public interface ICommandeItemService {
    CommandeItemDto save(CommandeItemRegisterDto commandeDto);
    CommandeItemDto update(Long id ,CommandeItemRegisterDto commandeDto);
    CommandeItemDto delete(Long id);
    CommandeItemDto getByid(Long id);
    List<CommandeItemDto> getAll();
}

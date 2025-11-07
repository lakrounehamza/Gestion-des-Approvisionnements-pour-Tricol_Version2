package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.CommandeDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.CommandeRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommandeService {
    CommandeDto save(CommandeRegisterDto commandeDto);
    CommandeDto update(Long id, CommandeRegisterDto commandeDto);
    CommandeDto delete(Long id);
    CommandeDto getById(Long id);
    Page<CommandeDto> getAll(Pageable pageable);
    Page<CommandeDto> getByStatut(String status, Pageable pageable);
}

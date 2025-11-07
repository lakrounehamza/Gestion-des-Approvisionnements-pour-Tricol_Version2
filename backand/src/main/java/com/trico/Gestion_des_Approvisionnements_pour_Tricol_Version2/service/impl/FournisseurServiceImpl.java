package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IFournisseurDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.FournisseurMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FournisseurServiceImpl implements IFournisseurService {

    private final FournisseurMapper fournisseurMapper;
    private final IFournisseurDao fournisseurDao;

    @Override
    public FournisseurDto save(FournisseurRegistreDTO fournisseurDto) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(fournisseurDto);
        Fournisseur fournisseurSave = fournisseurDao.save(fournisseur);
        return fournisseurMapper.toDto(fournisseurSave);
    }

    @Override
    public FournisseurDto update(Long id, FournisseurRegistreDTO fournisseurDto) {
        Fournisseur existingFournisseur = fournisseurDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé avec l'ID : " + id));

        existingFournisseur.setIce(fournisseurDto.getIce());
        existingFournisseur.setEmail(fournisseurDto.getEmail());
        existingFournisseur.setPhone(fournisseurDto.getPhone());
        existingFournisseur.setAdresse(fournisseurDto.getAdresse());

        Fournisseur updated = fournisseurDao.save(existingFournisseur);
        return fournisseurMapper.toDto(updated);
    }

    @Override
    public FournisseurDto delete(Long id) {
        Fournisseur fournisseur = fournisseurDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé pour suppression avec l'ID : " + id));
        fournisseurDao.delete(fournisseur);
        return fournisseurMapper.toDto(fournisseur);
    }

    @Override
    public FournisseurDto getById(Long id) {
        Fournisseur fournisseur = fournisseurDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Fournisseur non trouvé avec l'ID : " + id));
        return fournisseurMapper.toDto(fournisseur);
    }

    @Override
    public Page<FournisseurDto> getAll(Pageable pageable) {
        Page<Fournisseur> fournisseurs = fournisseurDao.findAll(pageable);
        if (fournisseurs.isEmpty()) {
            throw new NotFoundException("Aucun fournisseur trouvé.");
        }
        return fournisseurs.map(fournisseurMapper::toDto);
    }
}
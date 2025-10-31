package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IFournisseurDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.FournisseurRegistreDTO;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.FournisseurMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class FournisseurServiceImpl implements IFournisseurService {
    private final FournisseurMapper fournisseurMapper;
    private final IFournisseurDao fournisseurDao;

    @Override
    public FournisseurDto save(FournisseurRegistreDTO fournisseurDto) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(fournisseurDto);
        Fournisseur fournisseurSave = fournisseurDao.save(fournisseur);
        FournisseurDto fournisseurDtoSave = fournisseurMapper.toDto(fournisseurSave);
        return fournisseurDtoSave;
    }

    @Override
    public FournisseurDto update(Long id, FournisseurDto fournisseurDto) {
        if (!fournisseurDao.existsById(id)) {
            throw new RuntimeException("fournisseur non trouve ");
        }
        Fournisseur newFournisseur = fournisseurMapper.toEntity(fournisseurDto);
        Optional<Fournisseur> optionalFournisseur = fournisseurDao.findById(id);
        if (optionalFournisseur.isPresent()) {
            newFournisseur.setId(id);
            Fournisseur fournisseurRes = fournisseurDao.save(newFournisseur);
            return fournisseurMapper.toDto(fournisseurRes);
        } else
            return null;
    }

    @Override
    public FournisseurDto delete(Long id) {
        Fournisseur fournisseur = fournisseurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouve  pour suprission"));
        fournisseurDao.delete(fournisseur);
        return fournisseurMapper.toDto(fournisseur);
    }

    @Override
    public FournisseurDto getById(Long id) {
        Fournisseur  fournisseur = fournisseurDao.findById(id).orElseThrow(()->new RuntimeException("fournisser non trouve "));
            return fournisseurMapper.toDto(fournisseur);
    }

    @Override
    public List<FournisseurDto> getAll() {
        List<Fournisseur>  fournisseurs = fournisseurDao.findAll();
        return fournisseurs.stream().map(fournisseur -> fournisseurMapper.toDto(fournisseur)).collect(Collectors.toList());
    }
}

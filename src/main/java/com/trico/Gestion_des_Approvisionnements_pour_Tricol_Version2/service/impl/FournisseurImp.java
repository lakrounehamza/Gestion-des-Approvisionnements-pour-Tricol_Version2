package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IFournisseurDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.maper.FournisseurMaper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class FournisseurImp implements IFournisseurService {
    private final FournisseurMaper fournisseurMaper;
    private final IFournisseurDao fournisseurDao;

    @Override
    public FournisseurDto save(FournisseurDto fournisseurDto) {
        Fournisseur fournisseur = fournisseurMaper.toEntity(fournisseurDto);
        Fournisseur fournisseurSave = fournisseurDao.save(fournisseur);
        FournisseurDto fournisseurDtoSave = fournisseurMaper.toDto(fournisseurSave);
        return fournisseurDtoSave;
    }

    @Override
    public FournisseurDto update(Long id, FournisseurDto fournisseurDto) {
        if (!fournisseurDao.existsById(id)) {
            throw new RuntimeException("fournisseur non trouve ");
        }
        Fournisseur newFournisseur = fournisseurMaper.toEntity(fournisseurDto);
        Optional<Fournisseur> optionalFournisseur = fournisseurDao.findById(id);
        if (optionalFournisseur.isPresent()) {
            newFournisseur.setId(id);
            Fournisseur fournisseurRes = fournisseurDao.save(newFournisseur);
            return fournisseurMaper.toDto(fournisseurRes);
        } else
            return null;
    }

    @Override
    public FournisseurDto delete(Long id) {
        Fournisseur fournisseur = fournisseurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouve  pour suprission"));
        fournisseurDao.delete(fournisseur);
        return fournisseurMaper.toDto(fournisseur);
    }

    @Override
    public FournisseurDto getByid(Long id) {
        Fournisseur  fournisseur = fournisseurDao.findById(id).orElseThrow(()->new RuntimeException("fournisser non trouve "));
            return fournisseurMaper.toDto(fournisseur);
    }

    @Override
    public List<FournisseurDto> getAll() {
        List<Fournisseur>  fournisseurs = fournisseurDao.findAll();
        return fournisseurs.stream().map(fournisseur -> fournisseurMaper.toDto(fournisseur)).collect(Collectors.toList());
    }
}

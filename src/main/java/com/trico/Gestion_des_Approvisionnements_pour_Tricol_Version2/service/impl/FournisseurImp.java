package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.FournisseurDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Fournisseur;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IFournisseurService;

import java.util.List;

public class FournisseurImp implements IFournisseurService {
    @Override
    public FournisseurDto save(FournisseurDto fournisseurDto) {
        return null;

    }

    @Override
    public FournisseurDto update(String id, FournisseurDto fournisseurDto) {
        return null;
    }

    @Override
    public FournisseurDto delete(String id) {
        return null;
    }

    @Override
    public FournisseurDto getByid(String id) {
        return null;
    }

    @Override
    public List<FournisseurDto> getAll() {
        return List.of();
    }
}

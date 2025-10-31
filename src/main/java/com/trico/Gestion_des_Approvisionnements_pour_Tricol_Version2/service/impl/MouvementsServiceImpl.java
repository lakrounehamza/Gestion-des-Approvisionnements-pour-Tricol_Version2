package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.impl;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao.IMouvementsStockDao;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.MouvementsStockDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register.MouvementsStockRegisterDto;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.MouvementsStock;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.exceptions.NotFoundException;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.mappers.MouvementsStockMapper;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.service.interfaces.IMouvementsStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MouvementsServiceImpl implements IMouvementsStockService {

    private final IMouvementsStockDao mouvementsStockDao;
    private final MouvementsStockMapper mouvementsStockMapper;

    @Override
    public MouvementsStockDto save(MouvementsStockRegisterDto dto) {
        MouvementsStock mouvement = mouvementsStockMapper.toEntity(dto);
        MouvementsStock saved = mouvementsStockDao.save(mouvement);
        return mouvementsStockMapper.toDto(saved);
    }

    @Override
    public MouvementsStockDto update(Long id, MouvementsStockRegisterDto dto) {
        MouvementsStock existing = mouvementsStockDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le mouvement de stock avec l’ID " + id + " n’existe pas pour la modification."));

        existing.setDatemouvement(dto.getDatemouvement());
        existing.setQuantity(dto.getQuantity());
        existing.setQuantityMin(dto.getQuantityMin());
        existing.setProduit(dto.getProduit());
        existing.setReference(dto.getReference());
        existing.setType(dto.getType());

        MouvementsStock updated = mouvementsStockDao.save(existing);
        return mouvementsStockMapper.toDto(updated);
    }


    @Override
    public MouvementsStockDto delete(Long id) {
        MouvementsStock mouvement = mouvementsStockDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le mouvement de stock avec l’ID " + id + " n’existe pas pour la suppression."));
        mouvementsStockDao.delete(mouvement);
        return mouvementsStockMapper.toDto(mouvement);
    }

    @Override
    public MouvementsStockDto getById(Long id) {
        MouvementsStock mouvement = mouvementsStockDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Le mouvement de stock avec l’ID " + id + " n’existe pas."));
        return mouvementsStockMapper.toDto(mouvement);
    }

    @Override
    public List<MouvementsStockDto> getAll() {
        List<MouvementsStock> mouvements = mouvementsStockDao.findAll();
        if (mouvements.isEmpty()) {
            throw new NotFoundException("Aucun mouvement de stock trouvé.");
        }
        return mouvements.stream()
                .map(mouvementsStockMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<MouvementsStockDto> getByProduitId(Long id){
        return getAll().stream().filter(mouvementsStockDto -> mouvementsStockDto.getProduit().getId().equals(id)).collect(Collectors.toList());
    }
}

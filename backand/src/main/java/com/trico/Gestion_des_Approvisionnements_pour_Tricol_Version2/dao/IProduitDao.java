package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dao;

import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.entitys.Produit;
import com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.enums.CategorieEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProduitDao extends JpaRepository<Produit, Long> {

    // Find by category with pagination
    Page<Produit> findByCategorie(CategorieEnum categorie, Pageable pageable);

    // Search by name only (case insensitive)
    Page<Produit> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    // Custom search query - modify based on your actual Produit fields
    @Query("SELECT p FROM Produit p WHERE " +
            "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%')))")
    Page<Produit> searchByNom(@Param("nom") String nom, Pageable pageable);

    // If you want to search by description instead of reference
    @Query("SELECT p FROM Produit p WHERE " +
            "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Produit> searchByNomAndDescription(
            @Param("nom") String nom,
            @Param("description") String description,
            Pageable pageable);
}
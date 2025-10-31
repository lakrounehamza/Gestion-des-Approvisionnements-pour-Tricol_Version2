package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FournisseurRegistreDTO {

    @NotBlank(message = "Le nom de la société est obligatoire.")
    @Size(min = 2, max = 100, message = "Le nom de la société doit contenir entre 2 et 100 caractères.")
    private String societe;

    @NotBlank(message = "L’adresse est obligatoire.")
    @Size(max = 255, message = "L’adresse ne doit pas dépasser 255 caractères.")
    private String adresse;

    @NotBlank(message = "Le nom du contact est obligatoire.")
    @Size(min = 2, max = 100, message = "Le nom du contact doit contenir entre 2 et 100 caractères.")
    private String contact;

    @NotBlank(message = "L’adresse email est obligatoire.")
    @Email(message = "L’adresse email doit être valide.")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire.")
    @Pattern( regexp = "^(\\+\\d{1,3})?[- .]?\\d{2,4}([- .]?\\d{2,4}){2,3}$",
             message = "Le numéro de téléphone doit être valide (ex : +212XXXXXXXXX).")
    private String phone;

    @NotBlank(message = "La ville est obligatoire.")
    @Size(min = 2, max = 50, message = "Le nom de la ville doit contenir entre 2 et 50 caractères.")
    private String ville;

    @NotBlank(message = "Le code ICE est obligatoire.")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Le code ICE doit contenir uniquement des chiffres (entre 8 et 15 caractères).")
    private String ice;
}

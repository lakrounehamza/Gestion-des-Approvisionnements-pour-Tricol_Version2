package com.trico.Gestion_des_Approvisionnements_pour_Tricol_Version2.dto.register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FournisseuRregistreDTO {
    private String societe;
    private String adresse;
    private String contact;
    private String email;
    private String phone;
    private String ville;
    private String ice;
}

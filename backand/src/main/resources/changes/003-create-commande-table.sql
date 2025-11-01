CREATE TABLE commande
(
    id             BIGSERIAL PRIMARY KEY,
    statut         VARCHAR(50) NOT NULL,
    montant        DECIMAL(12, 2),
    date_commande  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fournisseur_id BIGINT,
    client_id      BIGINT,
    created_at     TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_commande_fournisseur FOREIGN KEY (fournisseur_id)
        REFERENCES fournisseur (id) ON DELETE SET NULL,
    CONSTRAINT fk_commande_client FOREIGN KEY (client_id)
        REFERENCES fournisseur (id) ON DELETE SET NULL
);
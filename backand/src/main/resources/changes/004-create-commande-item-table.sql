CREATE TABLE commande_item (
                               id BIGSERIAL PRIMARY KEY,
                               quantity INTEGER NOT NULL CHECK (quantity > 0),
                               prix_unitaire DECIMAL(10, 2),
                               date_item TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               commande_id BIGINT NOT NULL,
                               produit_id BIGINT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_commande_item_commande FOREIGN KEY (commande_id)
                                   REFERENCES commande(id) ON DELETE CASCADE,
                               CONSTRAINT fk_commande_item_produit FOREIGN KEY (produit_id)
                                   REFERENCES produit(id) ON DELETE RESTRICT
);
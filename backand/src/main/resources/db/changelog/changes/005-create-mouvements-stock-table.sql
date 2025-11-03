CREATE TABLE IF NOT EXISTS mouvements_stock (
                                  id  int PRIMARY KEY AUTO_INCREMENT,
                                  date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  quantity INTEGER NOT NULL,
                                  quantity_min INTEGER NOT NULL DEFAULT 10,
                                  type VARCHAR(50) NOT NULL,
                                  reference VARCHAR(100),
                                  produit_id BIGINT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_mouvements_stock_produit FOREIGN KEY (produit_id)
                                      REFERENCES produit(id) ON DELETE CASCADE
);
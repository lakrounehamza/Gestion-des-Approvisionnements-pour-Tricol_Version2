CREATE TABLE fournisseur (
                             id BIGSERIAL PRIMARY KEY,
                             societe VARCHAR(200) NOT NULL,
                             adresse VARCHAR(255),
                             contact VARCHAR(100),
                             email VARCHAR(150),
                             phone VARCHAR(20),
                             ville VARCHAR(100),
                             ice VARCHAR(50) UNIQUE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
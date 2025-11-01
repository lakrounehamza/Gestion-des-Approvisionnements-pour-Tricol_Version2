CREATE TABLE produit (
                         id BIGSERIAL PRIMARY KEY,
                         nom VARCHAR(200) NOT NULL,
                         prix DECIMAL(10, 2) NOT NULL,
                         description TEXT,
                         categorie VARCHAR(50) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
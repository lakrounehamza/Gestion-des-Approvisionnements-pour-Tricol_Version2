
DELIMITER $$
CREATE TRIGGER controller_commande
    AFTER INSERT ON commande_item
    FOR EACH ROW
BEGIN
    DECLARE v_stock INT DEFAULT 0;
	DECLARE v_to_consume INT DEFAULT 0;
	DECLARE v_id BIGINT;
	DECLARE v_qty INT;
	DECLARE v_price DOUBLE;
	DECLARE v_total_price DOUBLE DEFAULT 0;

	-- total disponible
    SELECT IFNULL(SUM(quantite),0) INTO v_stock
    FROM produit_item
    WHERE produit_id = NEW.produit_id;

    IF v_stock > 0 THEN
		-- limiter la quantité à ce qui est disponible
		IF NEW.quantity > v_stock THEN
			SET v_to_consume = v_stock;
    ELSE
			SET v_to_consume = NEW.quantity;
END IF;

-- consommer FIFO
WHILE v_to_consume > 0 DO
SELECT id, quantite, prix INTO v_id, v_qty, v_price
FROM produit_item
WHERE produit_id = NEW.produit_id AND quantite > 0
ORDER BY id ASC
    LIMIT 1;

IF v_id IS NULL THEN
				-- plus de lots trouvés (sécurité)
				SET v_to_consume = 0;
ELSE
				IF v_qty <= v_to_consume THEN
UPDATE produit_item SET quantite = 0 WHERE id = v_id;
SET v_total_price = v_total_price + (v_qty * v_price);
INSERT INTO mouvements_stock(datemouvement, quantity, price, type, produit_id)
VALUES (NOW(), v_qty, v_qty * v_price, 'SORTIE', NEW.produit_id);
SET v_to_consume = v_to_consume - v_qty;
                    --insert prduit_item pour client
iNSERT INTO produit_item (prix, quantite, produit_id) VALUES (v_price, v_qty, NEW.produit_id);
ELSE
UPDATE produit_item SET quantite = quantite - v_to_consume WHERE id = v_id;
SET v_total_price = v_total_price + (v_to_consume * v_price);
INSERT INTO mouvements_stock(datemouvement, quantity, price, type, produit_id)
VALUES (NOW(), v_to_consume, v_to_consume * v_price, 'SORTIE', NEW.produit_id);
SET v_to_consume = 0;
END IF;
END IF;
END WHILE;
END IF;

	-- v_total_price contient le montant total consommé si besoin
END$$
DELIMITER ;




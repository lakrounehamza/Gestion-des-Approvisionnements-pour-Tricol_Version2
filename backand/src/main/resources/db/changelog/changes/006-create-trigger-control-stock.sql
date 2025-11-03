DELIMITER $$
CREATE TRIGGER trigger_controle_stock
    BEFORE INSERT ON commande_item
    FOR EACH ROW
BEGIN
    DECLARE v_stock_actuel INT DEFAULT 0;
    DECLARE v_quantity_min INT DEFAULT 0;
    DECLARE v_produit_nom VARCHAR(200);
    DECLARE v_stock_apres INT DEFAULT 0;

    SELECT nom INTO v_produit_nom
    FROM produit
    WHERE id = NEW.produit_id;

    SELECT
        COALESCE(SUM(CASE WHEN type = 'ENTREE' THEN quantity ELSE 0 END), 0) -
        COALESCE(SUM(CASE WHEN type = 'SORTIE' THEN quantity ELSE 0 END), 0)
    INTO v_stock_actuel
    FROM mouvements_stock
    WHERE produit_id = NEW.produit_id;

    SELECT COALESCE(MAX(quantity_min), 10)
    INTO v_quantity_min
    FROM mouvements_stock
    WHERE produit_id = NEW.produit_id;

    IF v_stock_actuel < NEW.quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = CONCAT('Stock insuffisant pour le produit (ID: ', NEW.produit_id,
                                  '). Stock actuel: ', v_stock_actuel,
                                  ', Quantité demandée: ', NEW.quantity);
END IF;

SET v_stock_apres = v_stock_actuel - NEW.quantity;
INSERT INTO mouvements_stock (
    date_mouvement,
    quantity,
    quantity_min,
    type,
    reference,
    produit_id
) VALUES (
             NOW(),
             NEW.quantity,
             v_quantity_min,
             'SORTIE',
             CONCAT('CMD-', NEW.commande_id, '-ITEM-', NEW.id),
             NEW.produit_id
         );
END$$
DELIMITER ;

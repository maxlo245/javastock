-- Schema MySQL pour Javastock

CREATE TABLE IF NOT EXISTS article (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    categorie VARCHAR(100) NOT NULL,
    quantite INT NOT NULL CHECK (quantite >= 0),
    suppression_logique BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS coureur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS type_epreuve (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    coureur_id INT NOT NULL,
    type_epreuve_id INT NOT NULL,
    FOREIGN KEY (coureur_id) REFERENCES coureur(id) ON DELETE RESTRICT,
    FOREIGN KEY (type_epreuve_id) REFERENCES type_epreuve(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS reservation_article (
    reservation_id INT NOT NULL,
    article_id INT NOT NULL,
    quantite INT NOT NULL CHECK (quantite > 0),
    PRIMARY KEY (reservation_id, article_id),
    FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE RESTRICT
);

CREATE INDEX idx_reservation_coureur ON reservation(coureur_id);
CREATE INDEX idx_reservation_type_epreuve ON reservation(type_epreuve_id);
CREATE INDEX idx_reservation_article_article ON reservation_article(article_id);

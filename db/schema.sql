-- Schema PostgreSQL pour Javastock

CREATE TABLE IF NOT EXISTS article (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    categorie VARCHAR(100) NOT NULL,
    quantite INTEGER NOT NULL CHECK (quantite >= 0),
    suppression_logique BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS coureur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS type_epreuve (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    coureur_id INTEGER NOT NULL REFERENCES coureur(id) ON DELETE RESTRICT,
    type_epreuve_id INTEGER NOT NULL REFERENCES type_epreuve(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS reservation_article (
    reservation_id INTEGER NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
    article_id INTEGER NOT NULL REFERENCES article(id) ON DELETE RESTRICT,
    quantite INTEGER NOT NULL CHECK (quantite > 0),
    PRIMARY KEY (reservation_id, article_id)
);

CREATE INDEX IF NOT EXISTS idx_reservation_coureur ON reservation(coureur_id);
CREATE INDEX IF NOT EXISTS idx_reservation_type_epreuve ON reservation(type_epreuve_id);
CREATE INDEX IF NOT EXISTS idx_reservation_article_article ON reservation_article(article_id);


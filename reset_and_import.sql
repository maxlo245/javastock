-- Réinitialisation complète du schéma public
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- Création des tables
CREATE TABLE Coureur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL
);

CREATE TABLE TypeEpreuve (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Article (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    quantite INT NOT NULL,
    categorie VARCHAR(20) NOT NULL CHECK (categorie IN ('Textile', 'Boisson', 'DenreeSeche')),
    sl BOOLEAN DEFAULT FALSE
);

CREATE TABLE Textile (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    taille VARCHAR(10),
    couleur VARCHAR(20)
);

CREATE TABLE Boisson (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    volume INT
);

CREATE TABLE DenreeSeche (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    poids INT
);

CREATE TABLE Reservation (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    coureur_id INT REFERENCES Coureur(id),
    type_epreuve_id INT REFERENCES TypeEpreuve(id)
);

CREATE TABLE ReservationArticle (
    reservation_id INT REFERENCES Reservation(id),
    article_id INT REFERENCES Article(id),
    quantite INT NOT NULL,
    PRIMARY KEY (reservation_id, article_id)
);

-- Insertion de données
INSERT INTO Coureur (nom, prenom) VALUES
  ('Dupont', 'Jean'),
  ('Martin', 'Alice');

INSERT INTO TypeEpreuve (libelle) VALUES
  ('Marathon'),
  ('Trail');

INSERT INTO Article (libelle, quantite, categorie, sl) VALUES
  ('T-shirt L vert', 10, 'Textile', false),
  ('Bouteille 50cl', 6, 'Boisson', false),
  ('Barre énergétique 250g', 12, 'DenreeSeche', false);

INSERT INTO Textile (article_id, taille, couleur) VALUES
  (1, 'L', 'vert');

INSERT INTO Boisson (article_id, volume) VALUES
  (2, 50);

INSERT INTO DenreeSeche (article_id, poids) VALUES
  (3, 250);

INSERT INTO Reservation (date, coureur_id, type_epreuve_id) VALUES
  ('2026-02-11', 1, 1),
  ('2026-02-12', 2, 2);

INSERT INTO ReservationArticle (reservation_id, article_id, quantite) VALUES
  (1, 1, 2),
  (1, 2, 1),
  (2, 3, 3);

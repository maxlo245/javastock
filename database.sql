-- Script SQL de création de la base javastocks
CREATE DATABASE javastocks;
\c javastocks;

-- Table Coureur
CREATE TABLE Coureur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL
);

-- Table TypeEpreuve
CREATE TABLE TypeEpreuve (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

-- Table Article (classe mère)
CREATE TABLE Article (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    quantite INT NOT NULL,
    categorie VARCHAR(20) NOT NULL CHECK (categorie IN ('Textile', 'Boisson', 'DenreeSeche')),
    sl BOOLEAN DEFAULT FALSE
);

-- Table Textile (hérite de Article)
CREATE TABLE Textile (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    taille VARCHAR(10),
    couleur VARCHAR(20)
);

-- Table Boisson (hérite de Article)
CREATE TABLE Boisson (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    volume INT
);

-- Table DenreeSeche (hérite de Article)
CREATE TABLE DenreeSeche (
    article_id INT PRIMARY KEY REFERENCES Article(id),
    poids INT
);

-- Table Reservation
CREATE TABLE Reservation (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    coureur_id INT REFERENCES Coureur(id),
    type_epreuve_id INT REFERENCES TypeEpreuve(id)
);

-- Table ReservationArticle (relation n-n)
CREATE TABLE ReservationArticle (
    reservation_id INT REFERENCES Reservation(id),
    article_id INT REFERENCES Article(id),
    quantite INT NOT NULL,
    PRIMARY KEY (reservation_id, article_id)
);

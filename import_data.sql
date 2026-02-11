-- Insertion dans Coureur
INSERT INTO Coureur (nom, prenom) VALUES
  ('Dupont', 'Jean'),
  ('Martin', 'Alice');

-- Insertion dans TypeEpreuve
INSERT INTO TypeEpreuve (libelle) VALUES
  ('Marathon'),
  ('Trail');

-- Insertion dans Article
INSERT INTO Article (libelle, quantite, categorie, sl) VALUES
  ('T-shirt L vert', 10, 'Textile', false),
  ('Bouteille 50cl', 6, 'Boisson', false),
  ('Barre énergétique 250g', 12, 'DenreeSeche', false);

-- Insertion dans Textile (lié au premier article)
INSERT INTO Textile (article_id, taille, couleur) VALUES
  (1, 'L', 'vert');

-- Insertion dans Boisson (lié au deuxième article)
INSERT INTO Boisson (article_id, volume) VALUES
  (2, 50);

-- Insertion dans DenreeSeche (lié au troisième article)
INSERT INTO DenreeSeche (article_id, poids) VALUES
  (3, 250);

-- Insertion dans Reservation
INSERT INTO Reservation (date, coureur_id, type_epreuve_id) VALUES
  ('2026-02-11', 1, 1),
  ('2026-02-12', 2, 2);

-- Insertion dans ReservationArticle
INSERT INTO ReservationArticle (reservation_id, article_id, quantite) VALUES
  (1, 1, 2),
  (1, 2, 1),
  (2, 3, 3);

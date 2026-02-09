# Documentation technique JavaStocks

## Structure du projet
- `src/JavaStocks/` : code source Java
- `pom.xml` : configuration Maven
- `database.sql` : script de création de la base PostgreSQL
- `diagramme_classes.puml` : diagramme de classes PlantUML

## Entités principales
- Article (héritée par Textile, Boisson, DenreeSeche)
- Coureur
- TypeEpreuve
- Reservation
- ReservationArticle (relation n-n)

## Fonctionnalités
- CRUD complet sur chaque entité
- Interface graphique Swing
- Connexion PostgreSQL (DAO)
- Gestion des exceptions et ressources

## Compilation et exécution
Voir le README.md principal.

## Tests unitaires
Les tests sont à placer dans `src/test/java/JavaStocks/` et à exécuter avec Maven (`mvn test`).

## Auteur
maxlo245

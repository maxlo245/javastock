# README interne – Documentation technique JavaStocks

Ce document s'adresse aux développeurs et contributeurs du projet JavaStock. Il détaille l'architecture, les conventions, le workflow de développement, les tests, l'intégration continue, et les conseils pour contribuer efficacement.

## Structure du projet
- `src/JavaStocks/` : code source Java (modèles, DAO, menus, utilitaires)
- `pom.xml` : configuration Maven (dépendances, build, plugins)
- `database.sql` : script de création de la base PostgreSQL
- `diagramme_classes.puml` : diagramme de classes PlantUML
- `bin/` : fichiers compilés
- `lib/` : bibliothèques tierces (ex : JDBC PostgreSQL)
- `db/` : scripts SQL
- `run.bat` / `test_db.bat` : scripts d'exécution et de test

## Architecture et entités principales

L'application suit une architecture en couches :

1. **Couche Présentation** : classes `*Menu.java` (Swing)
2. **Couche Métier** : modèles (`Article`, `Coureur`, `TypeEpreuve`, `Reservation`, `ReservationArticle`)
3. **Couche DAO** : accès aux données (`ArticleDAO`, `CoureurDAO`, etc.)
4. **Couche Base de Données** : PostgreSQL

### Détail des entités
- `Article` (héritée par `Textile`, `Boisson`, `DenreeSeche`) : gestion des stocks
- `Coureur` : informations sur les participants
- `TypeEpreuve` : types d'épreuves (Marathon, Trail...)
- `Reservation` : réservation d'articles pour un coureur
- `ReservationArticle` : relation n-n entre réservation et article

## Fonctionnalités principales
- CRUD complet sur chaque entité (DAO)
- Interface graphique Swing (menus dédiés)
- Connexion PostgreSQL via JDBC
- Gestion rigoureuse des exceptions et ressources (try-with-resources recommandé)
- Validation des formulaires côté client
- Suppression logique des articles
- Multi-sélection d'articles pour les réservations
- Affichage en temps réel des disponibilités

## Compilation et exécution
Se référer au README principal pour les commandes de build et d'exécution. Utiliser Maven de préférence pour garantir la cohérence des dépendances.

### Commandes utiles
- Compilation : `mvn clean compile`
- Exécution : `run.bat` ou `java -cp "lib/postgresql-42.7.1.jar;bin/JavaStocks/JavaStocks" JavaStocks.MainMenu`
- Tests : `mvn test` ou `test_db.bat`

## Tests unitaires et qualité

Les tests doivent être placés dans `src/test/java/JavaStocks/`.
- Utiliser JUnit pour les tests unitaires.
- Tester chaque DAO, chaque logique métier.
- Utiliser des mocks pour la base de données (ex : Mockito).
- Vérifier la couverture de code (plugin Maven `jacoco`).
- Les scripts `test_db.bat` permettent de tester la connexion et les opérations CRUD.

## Workflow de développement

1. Créer une branche pour chaque fonctionnalité ou correction (`feature/xxx`, `fix/xxx`)
2. Commits atomiques avec messages explicites (en français ou anglais)
3. Pull request pour toute évolution majeure
4. Revue de code obligatoire avant merge
5. Tests automatiques à chaque PR
6. Respecter la structure des packages et la convention de nommage Java
7. Documenter les méthodes publiques (JavaDoc)
8. Utiliser des TODO/FIXME pour signaler les points à améliorer

## Intégration continue

Configurer GitHub Actions ou un autre outil CI pour :
- Lancer les tests à chaque push/PR
- Vérifier la compilation
- Générer les rapports de couverture
- Refuser le merge si les tests échouent

## Conseils pour contribuer
- Lire ce README avant toute contribution
- Respecter les conventions de code
- Ajouter des tests pour chaque nouvelle fonctionnalité
- Préférer les issues pour discuter des évolutions
- Utiliser les pull requests pour proposer des modifications

## Auteur principal
maxlo245

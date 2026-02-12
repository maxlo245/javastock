# JavaStock

Projet de gestion de stocks pour une course (Java, Swing, PostgreSQL).

## Description

JavaStock est une application de gestion complète permettant de gérer les stocks d'articles, les coureurs, les types d'épreuve et les réservations pour des événements sportifs. L'application utilise une interface graphique Swing moderne avec des formulaires client et une base de données PostgreSQL pour la persistence des données.

javastock/
├── src/JavaStocks/          # Code source Java
│   ├── MainMenu.java        # Menu principal
│   ├── DatabaseConnection.java  # Connexion PostgreSQL
│   ├── ArticleMenu.java     # Formulaire articles
│   ├── CoureurMenu.java     # Formulaire coureurs
│   ├── TypeEpreuveMenu.java # Formulaire types d'épreuve
│   ├── ReservationMenu.java # Formulaire réservations
│   ├── Article.java         # Modèle Article
│   ├── ArticleDAO.java      # DAO Article
│   ├── Coureur.java         # Modèle Coureur
│   ├── CoureurDAO.java      # DAO Coureur
│   ├── TypeEpreuve.java     # Modèle TypeEpreuve
│   ├── TypeEpreuveDAO.java  # DAO TypeEpreuve
│   ├── Reservation.java     # Modèle Reservation
│   ├── ReservationDAO.java  # DAO Reservation
│   └── ReservationArticle.java  # Modèle ReservationArticle
├── bin/JavaStocks/JavaStocks/  # Fichiers compilés
├── lib/                     # Bibliothèques
│   └── postgresql-42.7.1.jar
├── db/                      # Scripts SQL
│   └── schema.sql
├── run.bat                  # Script de lancement Windows
├── test_db.bat              # Script de test de la base
└── pom.xml                  # Configuration Maven

```


## Installation

### Prérequis

- Java JDK 21 ou supérieur
- PostgreSQL 12 ou supérieur
- Maven (optionnel)

### Étapes

1. **Cloner le projet**

```bash
git clone <url-du-repo>
cd javastock
```

2. **Configurer PostgreSQL**

```bash
# Créer la base de données
createdb -U admin javastock

# Initialiser le schéma
psql -U admin -d javastock -f db/schema.sql
```

3. **Compiler le projet**

Avec Maven :

```bash
mvn clean compile
```

Ou manuellement :

```bash
javac -cp "lib/postgresql-42.7.1.jar;src" -d bin/JavaStocks/JavaStocks src/JavaStocks/*.java
```

4. **Lancer l'application**

Windows :

```bash
run.bat
```

Manuel :

```bash
java -cp "lib/postgresql-42.7.1.jar;bin/JavaStocks/JavaStocks" JavaStocks.MainMenu
```

## Utilisation

1. **Lancer l'application** en double-cliquant sur `run.bat` ou via la ligne de commande
2. **Sélectionner une catégorie** dans le menu principal
3. **Remplir le formulaire** avec les informations requises
4. **Cliquer sur le bouton vert "Envoyer"** pour sauvegarder en base de données
5. Un **message de confirmation** s'affiche après chaque ajout réussi
6. Le formulaire se **réinitialise automatiquement** pour permettre un nouvel ajout

## Captures d'écran

L'application dispose d'une interface graphique moderne avec :

- Titre en gras au-dessus de chaque formulaire
- Labels alignés et champs de saisie larges
- Bouton "Envoyer" en vert mis en évidence
- Messages de confirmation et d'erreur clairs
- Espacement agréable entre les éléments

## Tests

Pour tester la connexion à la base de données et les opérations CRUD :

```bash
test_db.bat
```

Ce script teste :

- La connexion à PostgreSQL
- Les opérations CRUD sur les coureurs
- Les opérations CRUD sur les articles
- Les opérations CRUD sur les types d'épreuve

## Technologies Utilisées

- **Java 21** - Langage de programmation
- **Swing** - Interface graphique
- **PostgreSQL 17** - Base de données
- **JDBC** - Connexion base de données
- **Maven** - Gestion des dépendances (optionnel)

## Architecture

L'application suit une architecture en couches :

1. **Couche Présentation** (`*Menu.java`) - Formulaires Swing
2. **Couche Métier** (`*.java` modèles) - Objets métier
3. **Couche DAO** (`*DAO.java`) - Accès aux données
4. **Couche Base de Données** - PostgreSQL

## Fonctionnalités à Venir

- Article en rupture / réservation en attente
- Consulter l'historique des réservations
- Export des données en PDF/Excel
- Statistiques et tableaux de bord

## Auteur

**Maxime LAURENT**

## Licence

MIT License

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou une pull request.

## Support

Pour toute question ou problème, veuillez ouvrir une issue sur le dépôt GitHub.

---

**Date de dernière mise à jour**: 09/02/2026
**Version**: 1.0.0

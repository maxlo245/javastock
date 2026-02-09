===========================================
  JAVASTOCK - IMPLEMENTATION COMPLETE
===========================================
BASE DE DONNEES PostgreSQL
--------------------------
- Base: javastock
- Utilisateur: admin
- Mot de passe: root
- Port: 5432
TABLES CREEES:
- article (id, libelle, categorie, quantite, suppression_logique)
- coureur (id, nom, prenom)
- type_epreuve (id, libelle)
- reservation (id, date, coureur_id, type_epreuve_id)
- reservation_article (reservation_id, article_id, quantite)
MENUS IMPLEMENTES (Interface Swing)
------------------------------------
1. GESTION DES ARTICLES
   - Ajouter un article (libelle, categorie, quantite)
   - Modifier un article existant
   - Supprimer un article (suppression logique)
   - Actualiser la liste
   - Affichage en tableau avec tous les articles
2. GESTION DES COUREURS
   - Ajouter un coureur (nom, prenom)
   - Modifier un coureur existant
   - Supprimer un coureur
   - Actualiser la liste
   - Affichage en tableau
3. GESTION DES TYPES D'EPREUVE
   - Ajouter un type d'epreuve (libelle)
   - Modifier un type existant
   - Supprimer un type
   - Actualiser la liste
   - Affichage en tableau
4. GESTION DES RESERVATIONS
   - Creer une reservation (date, coureur, type epreuve, articles)
   - Consulter les details d'une reservation
   - Annuler une reservation
   - Selection interactive des articles avec quantites
   - Actualiser la liste
   - Affichage en tableau avec resume
FICHIERS CLES
-------------
- DatabaseConnection.java : Gestion connexion PostgreSQL
- MainMenu.java : Menu principal avec connexion DB
- ArticleMenu.java : Interface complete CRUD Articles
- CoureurMenu.java : Interface complete CRUD Coureurs
- TypeEpreuveMenu.java : Interface complete CRUD Types
- ReservationMenu.java : Interface complete CRUD Reservations
SCRIPTS UTILES
--------------
- run.bat : Lance l'application JavaStock
- test_db.bat : Teste la connexion et les operations CRUD
COMMENT UTILISER
----------------
1. Lancez run.bat ou double-cliquez dessus
2. Le Menu Principal s'ouvre
3. Selectionnez une option et cliquez sur Valider
4. Chaque menu ouvre une nouvelle fenetre avec tableau et boutons
5. Toutes les operations sont sauvegardees en base de donnees
FONCTIONNALITES
---------------
✓ Connexion automatique a PostgreSQL au demarrage
✓ Tous les CRUD fonctionnels et relies a la base
✓ Interface graphique Swing complete
✓ Gestion d'erreurs avec messages clairs
✓ Actualisation automatique des tableaux
✓ Selection interactive pour les reservations
✓ Validation des donnees
===========================================
  APPLICATION PRETE A L'EMPLOI
===========================================

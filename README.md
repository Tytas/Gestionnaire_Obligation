# Gestionnaire d'Obligations

Application JavaFX pour la gestion d'obligations financières.

## Prérequis

- Java 21 ou supérieur
- Maven 3.6 ou supérieur

## Installation et compilation

1. Clonez le projet :
```bash
git clone <url-du-repo>
cd Gestionnaire_Obligation
```

2. Compilez le projet avec Maven :
```bash
mvn clean compile
```

3. Exécutez l'application :
```bash
mvn javafx:run
```

## Autres commandes utiles

- **Tests** : `mvn test`
- **Package** : `mvn package`
- **Nettoyage** : `mvn clean`

## Structure du projet

- `src/main/java/` : Code source Java
- `src/main/resources/` : Ressources (FXML, CSS, images)
- `src/test/java/` : Tests unitaires
- `target/` : Fichiers compilés (généré par Maven)

## Dépendances principales

- JavaFX 21.0.2
- iText 7.1.4 (génération PDF)
- Jackson 2.19.0 (traitement JSON)
- JUnit 5 (tests)
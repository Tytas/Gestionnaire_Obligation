# LOCIF GESTION - Guide d'utilisation

## 🚀 Lancement de l'application

# 🖥️ Installation & Prérequis

- **Java 17** (LTS) doit être installé : [Télécharger Java](https://adoptium.net/)
- **Maven 3.x** : [Télécharger Maven](https://maven.apache.org/download.cgi)
- Fonctionne sous **Windows**, **Linux** et **macOS** (profils automatiques)
### Méthode recommandée
Double-cliquez sur **"LocifGestion.bat"**

### Alternative
Double-cliquez sur le raccourci **"Locif Gestion.lnk"**

### Fonctionnalités
- ✅ Lancement automatique (aucune touche à presser)
- ✅ Aucun terminal visible pendant l'utilisation

# 📦 Lancement avancé

Après compilation, le JAR exécutable est disponible dans `target/` :
```bash
java -jar target/Locif-Gestion-1.0.0-executable.jar
```
```
Gestionnaire_Obligation/
├── LocifGestion.bat                     ← Lanceur principal ⭐
├── Locif Gestion.lnk                    ← Raccourci Windows
├── pom.xml                              ← Configuration Maven complète
├── README_UTILISATION.md                ← Ce guide
├── src/                                 ← Code source JavaFX
│   ├── main/java/mypackage/            ← Classes Java
│   └── main/resources/                  ← Ressources (FXML, CSS, images)
├── data/                                ← Données de l'application
└── target/                              ← Fichiers compilés
```

# 🗄️ Structure des données

Les dossiers `data/applicants`, `data/families`, `data/groups`, `data/investors`, `data/obligations` contiennent les fichiers JSON de chaque entité métier.
## 🔧 Développement avec Maven

### Compilation
```bash
mvn clean compile
```

### Lancement en mode développement
```bash
mvn javafx:run
```

### Tests
```bash
mvn test
```

### Package complet (JAR avec dépendances)
```bash
mvn clean package
```

### Package optimisé pour production
```bash
mvn clean package -P production
```

# 🧩 Modules principaux

- **Contrôleurs** : ControllerSouscripteurs, ControllerObligations, ControllerGroups, ControllerFamilies, ControllerEmetteurs, ControllerMenu, ControllerHome
- **Modèles** : Applicant, Family, Group, InvestorLP, InvestorNP, Obligation
- **Vues** : Fichiers FXML pour chaque opération (consultation, ajout, édition)
## 📚 Bibliothèques incluses

### Fonctionnalités métier
# 🔒 Sécurité & Confidentialité

- Toutes les données sont stockées localement en JSON.
- Aucune transmission externe, respect de la confidentialité.

# ⚠️ Limitations

- Pas de gestion multi-utilisateur.
- Pas de synchronisation cloud.

# 📞 Support

Pour toute question : dev@locif.com

# 📄 Licence

Licence propriétaire Locif. Voir [licence](https://locif.com/licence).
- **Apache POI (5.2.5)** - Génération et lecture Excel
- **Jackson (2.16.1)** - Traitement JSON des données

### Utilitaires
- **SLF4J (2.0.9)** - Logging professionnel
- **Apache Commons (3.14.0)** - Utilitaires Java
- **JUnit 5 (5.10.1)** - Tests unitaires
- **TestFX (4.0.18)** - Tests JavaFX

## 🎯 Configuration technique

### Java
- **Version :** Java 17 (LTS)
- **Encodage :** UTF-8
- **Maven :** 3.x

### Profils de compilation
- **development** (défaut) - Debug activé
- **production** - Optimisé et minifié
- **windows/linux/mac** - Configuration automatique par OS

### Plugins configurés
- **javafx-maven-plugin** - Lancement facile
- **maven-shade-plugin** - JAR exécutable
- **maven-surefire-plugin** - Tests automatisés
- **maven-source-plugin** - Sources JAR
- **maven-javadoc-plugin** - Documentation

## ✨ Fonctionnalités de l'application
- Gestion complète des obligations
- Export PDF avec mise en page professionnelle
- Export Excel avec formatage avancé
- Interface graphique moderne JavaFX
- Sauvegarde JSON des données
- Système de logging intégré

## 🛠️ Commandes utiles

```bash
# Nettoyer le projet
mvn clean

# Compiler seulement
mvn compile

# Lancer l'application
mvn javafx:run

# Tests complets
mvn clean test

# Package final
mvn clean package

# Package avec profil production
mvn clean package -P production

# Générer la documentation
mvn javadoc:javadoc
```

---
**Locif Gestion** - Application JavaFX de gestion des obligations  
Version finale avec pom.xml complet et toutes les dépendances

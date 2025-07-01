# Script PowerShell pour la gestion du projet Maven
param([string]$Command = "help")

Write-Host "=== Gestionnaire d'Obligations - Maven Helper ===" -ForegroundColor Green

switch ($Command.ToLower()) {
    "help" {
        Write-Host "Commandes disponibles:" -ForegroundColor Yellow
        Write-Host "  help      - Affiche cette aide"
        Write-Host "  check     - Verifie l'installation de Maven"
        Write-Host "  install   - Instructions d'installation de Maven"
        Write-Host "  compile   - Compile le projet"
        Write-Host "  run       - Lance l'application"
        Write-Host "  test      - Execute les tests"
        Write-Host "  package   - Cree un JAR executable"
        Write-Host "  clean     - Nettoie les fichiers compiles"
    }
    "check" {
        try {
            mvn --version
            Write-Host "Maven est installe et fonctionne" -ForegroundColor Green
        } catch {
            Write-Host "Maven n'est pas installe" -ForegroundColor Red
            Write-Host "Utilisez: .\maven-helper.ps1 install"
        }
    }
    "install" {
        Write-Host "Instructions d'installation de Maven:" -ForegroundColor Yellow
        Write-Host "1. Telechargez Maven depuis: https://maven.apache.org/download.cgi"
        Write-Host "2. Extrayez dans C:\apache-maven-3.9.x"
        Write-Host "3. Ajoutez C:\apache-maven-3.9.x\bin au PATH"
        Write-Host "4. Redemarrez PowerShell"
    }
    "compile" { mvn clean compile }
    "run" { mvn javafx:run }
    "test" { mvn test }
    "package" { mvn package }
    "clean" { mvn clean }
    default {
        Write-Host "Commande inconnue: $Command" -ForegroundColor Red
        Write-Host "Utilisez 'help' pour voir les commandes disponibles"
    }
}

@echo off
REM =============================================
REM LOCIF GESTION - LANCEUR FINAL DIRECT
REM Lancement direct de l'application JavaFX
REM =============================================

cd /d "%~dp0"

REM Vérifier que Java est installé
java -version >nul 2>&1
if errorlevel 1 (
    echo ERREUR: Java n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)

REM Vérifier que Maven est installé
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERREUR: Maven n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)

REM Lancer l'application JavaFX
mvn javafx:run

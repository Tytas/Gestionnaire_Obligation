@echo off
REM =============================================
REM LOCIF GESTION - LANCEUR FINAL DIRECT
REM Lancement direct de l'application JavaFX
REM =============================================

cd /d "%~dp0"

REM Vérification rapide
echo.
echo ==========================================
echo    LOCIF GESTION - DEMARRAGE
echo ==========================================
echo.

REM Lancer directement l'application avec Maven
echo Lancement de l'application...
mvn clean compile
echo.
mvn javafx:run

echo.
echo ==========================================
echo    APPLICATION FERMEE
echo ==========================================
echo.
timeout /t 3

exit

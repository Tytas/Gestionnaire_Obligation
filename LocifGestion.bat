@echo off
REM =============================================
REM LOCIF GESTION - LANCEUR FINAL DIRECT
REM Lancement direct de l'application JavaFX
REM =============================================

cd /d "%~dp0"

REM Lancer l'application JavaFX
mvn javafx:run

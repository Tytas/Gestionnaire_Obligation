#!/usr/bin/env pwsh
# Script de lancement de l'application Gestionnaire d'Obligations
# Ce script compile et lance l'application JavaFX avec Maven

Write-Host "🚀 Lancement de l'application Gestionnaire d'Obligations..." -ForegroundColor Green

try {
    # Compilation Maven
    Write-Host "📦 Compilation en cours..." -ForegroundColor Yellow
    mvn clean compile -q
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Erreur lors de la compilation!" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "✅ Compilation réussie!" -ForegroundColor Green
    
    # Lancement de l'application
    Write-Host "🎯 Lancement de l'application..." -ForegroundColor Yellow
    mvn javafx:run
    
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "=== Configuration finale de Locif Gestion ===" -ForegroundColor Green

$currentDir = Get-Location
$desktopPath = [Environment]::GetFolderPath("Desktop")

# Chemins des fichiers
$icoPath = Join-Path $currentDir "Locif_Gestion.ico"
$vbsPath = Join-Path $currentDir "LocifGestion.vbs"
$finalShortcut = Join-Path $currentDir "Locif Gestion"
$desktopShortcut = Join-Path $desktopPath "Locif Gestion.lnk"

Write-Host "1. Creation du raccourci final..." -ForegroundColor Yellow

# Supprimer l'ancien raccourci s'il existe
$oldShortcut = Join-Path $currentDir "Locif Gestion.lnk"
if (Test-Path $oldShortcut) {
    Remove-Item $oldShortcut -Force
}

# Créer le nouveau raccourci optimisé
$WshShell = New-Object -comObject WScript.Shell
$Shortcut = $WshShell.CreateShortcut("$finalShortcut.lnk")
$Shortcut.TargetPath = "wscript.exe"
$Shortcut.Arguments = "`"$vbsPath`""
$Shortcut.WorkingDirectory = $currentDir
$Shortcut.IconLocation = "$icoPath,0"
$Shortcut.Description = "Locif Gestion - Application de gestion"
$Shortcut.WindowStyle = 7
$Shortcut.Save()

Write-Host "2. Copie sur le Bureau..." -ForegroundColor Yellow

# Copier sur le Bureau
try {
    Copy-Item "$finalShortcut.lnk" $desktopShortcut -Force
    Write-Host "Raccourci copie sur le Bureau!" -ForegroundColor Green
} catch {
    Write-Host "Impossible de copier sur le Bureau" -ForegroundColor Yellow
}

Write-Host "3. Nettoyage..." -ForegroundColor Yellow

# Supprimer l'ancien exe
$exeToRemove = Join-Path $currentDir "Locif Gestion.exe"
if (Test-Path $exeToRemove) {
    Remove-Item $exeToRemove -Force
    Write-Host "Ancien .exe supprime" -ForegroundColor Green
}

Write-Host ""
Write-Host "=== Configuration terminee ===" -ForegroundColor Green
Write-Host "Votre application est accessible via:" -ForegroundColor Cyan
Write-Host "• Locif Gestion dans le dossier du projet" -ForegroundColor White
Write-Host "• Locif Gestion sur le Bureau" -ForegroundColor White
Write-Host ""
Write-Host "Caracteristiques:" -ForegroundColor Cyan
Write-Host "✓ Logo Lucky Oldstone comme icone" -ForegroundColor White
Write-Host "✓ Nom propre sans extension" -ForegroundColor White
Write-Host "✓ Ecran de chargement avec logo" -ForegroundColor White
Write-Host "✓ Aucun terminal visible" -ForegroundColor White
Write-Host "✓ Application au premier plan" -ForegroundColor White
Write-Host ""
pause

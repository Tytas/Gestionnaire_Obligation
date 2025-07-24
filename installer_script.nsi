; filepath: installer_script.nsi
!include "LogicLib.nsh"

!define APP_NAME "Gestionnaire Obligation"
!define APP_VERSION "1.0"
!define PUBLISHER "Votre Societe"

Name "${APP_NAME}"
OutFile "GestionnaireObligation_Setup.exe"
InstallDir "$PROGRAMFILES\${APP_NAME}"

Page license
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

; Verification des prerequis
Function .onInit
    nsExec::ExecToStack 'java -version'
    Pop $0
    ${If} $0 != 0
        MessageBox MB_YESNO "Java n'est pas installe. Voulez-vous le telecharger ?" IDYES download_java IDNO exit_install
        download_java:
            ExecShell "open" "https://github.com/adoptium/temurin17-binaries/releases"
            Goto exit_install
        exit_install:
            Quit
    ${EndIf}

    nsExec::ExecToStack 'mvn.cmd -version'
    Pop $0
    ${If} $0 != 0
        MessageBox MB_YESNO "Maven n'est pas installe. Voulez-vous le telecharger ?" IDYES download_maven IDNO continue_install
        download_maven:
            ExecShell "open" "https://archive.apache.org/dist/maven/maven-3/"
        continue_install:
    ${EndIf}
FunctionEnd

Section "Installation" SEC01
    SetOutPath $INSTDIR

    ; Copier TOUT le contenu (avec exclusions)
    DetailPrint "Copie de tous les fichiers du projet..."
    File /r /x "*.nsi" /x "*.exe" /x "*.log" /x "*.tmp" /x ".git" /x "*.bak" /x "Thumbs.db" "*.*"

    ; Creer script de lancement simple
    DetailPrint "Creation du script de lancement simple..."
    FileOpen $0 "$INSTDIR\launch.bat" w
    FileWrite $0 "@echo off$\r$\n"
    FileWrite $0 "cd /d $\"%~dp0$\"$\r$\n"
    FileWrite $0 "mvn javafx:run$\r$\n"
    FileClose $0

    ; Creer script de lancement avec splash propre
    DetailPrint "Creation du script de lancement avec splash..."
    FileOpen $0 "$INSTDIR\launch_with_splash.vbs" w
    FileWrite $0 "Set WshShell = CreateObject($\"WScript.Shell$\")$\r$\n"
    FileWrite $0 "Set fso = CreateObject($\"Scripting.FileSystemObject$\")$\r$\n"
    FileWrite $0 "$\r$\n"
    FileWrite $0 "' Obtenir le chemin vers l'image$\r$\n"
    FileWrite $0 "currentDir = fso.GetParentFolderName(WScript.ScriptFullName)$\r$\n"
    FileWrite $0 "imagePath = currentDir & $\"\src\main\resources\images\GroupeTurquoise.png$\"$\r$\n"
    FileWrite $0 "$\r$\n"
    FileWrite $0 "' Creer un script PowerShell temporaire pour afficher l'image de chargement$\r$\n"
    FileWrite $0 "psScript = currentDir & $\"\temp_loading.ps1$\"$\r$\n"
    FileWrite $0 "Set psFile = fso.CreateTextFile(psScript, True)$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"Add-Type -AssemblyName System.Windows.Forms$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"Add-Type -AssemblyName System.Drawing$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form = New-Object System.Windows.Forms.Form$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.Size = New-Object System.Drawing.Size(300, 200)$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.StartPosition = 'CenterScreen'$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.FormBorderStyle = 'None'$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.TopMost = $$true$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.BackColor = [System.Drawing.Color]::White$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$pictureBox = New-Object System.Windows.Forms.PictureBox$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$pictureBox.Image = [System.Drawing.Image]::FromFile('$\" & imagePath & $\"')$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$pictureBox.SizeMode = 'Zoom'$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$pictureBox.Size = New-Object System.Drawing.Size(250, 150)$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$pictureBox.Location = New-Object System.Drawing.Point(25, 25)$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.Controls.Add($$pictureBox)$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.Show()$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.Refresh()$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"Start-Sleep -Seconds 3$\"$\r$\n"
    FileWrite $0 "psFile.WriteLine $\"$$form.Close()$\"$\r$\n"
    FileWrite $0 "psFile.Close$\r$\n"
    FileWrite $0 "$\r$\n"
    FileWrite $0 "' Lancer l'ecran de chargement et Maven en parallele$\r$\n"
    FileWrite $0 "WshShell.Run $\"powershell.exe -WindowStyle Hidden -ExecutionPolicy Bypass -File $\" & Chr(34) & psScript & Chr(34), 0, False$\r$\n"
    FileWrite $0 "WScript.Sleep 500$\r$\n"
    FileWrite $0 "' Lancer Maven COMPLETEMENT cache$\r$\n"
    FileWrite $0 "WshShell.Run $\"cmd /c cd /d $\" & Chr(34) & currentDir & Chr(34) & $\" && start /b mvn javafx:run$\", 0, False$\r$\n"
    FileClose $0

    ; CREER LE DESINSTALLEUR EN PREMIER
    WriteUninstaller "$INSTDIR\Uninstall.exe"

    ; Puis creer les raccourcis (le desinstalleur existe maintenant)
    CreateShortCut "$DESKTOP\${APP_NAME}.lnk" "$INSTDIR\launch_with_splash.vbs" "" "$INSTDIR\Locif_Gestion.ico"

    CreateDirectory "$SMPROGRAMS\${APP_NAME}"
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME}.lnk" "$INSTDIR\launch_with_splash.vbs" "" "$INSTDIR\Locif_Gestion.ico"
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME} (demarrage rapide).lnk" "$INSTDIR\launch.bat" "" "$INSTDIR\Locif_Gestion.ico"
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\Desinstaller.lnk" "$INSTDIR\Uninstall.exe"

    ; Enregistrement Windows avec icone
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "DisplayName" "${APP_NAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "UninstallString" "$INSTDIR\Uninstall.exe"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "Publisher" "${PUBLISHER}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "DisplayVersion" "${APP_VERSION}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "DisplayIcon" "$INSTDIR\Locif_Gestion.ico"
SectionEnd

; Section de desinstallation
Section "Uninstall"
    Delete "$DESKTOP\${APP_NAME}.lnk"
    RMDir /r "$SMPROGRAMS\${APP_NAME}"
    RMDir /r "$INSTDIR"
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}"
SectionEnd
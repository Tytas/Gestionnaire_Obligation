Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Obtenir le chemin vers l'image
currentDir = fso.GetParentFolderName(WScript.ScriptFullName)
imagePath = currentDir & "\src\main\resources\images\lucky_oldstone_logo.jpeg"

' Créer un script PowerShell temporaire pour afficher l'image de chargement
psScript = currentDir & "\temp_loading.ps1"
Set psFile = fso.CreateTextFile(psScript, True)
psFile.WriteLine "Add-Type -AssemblyName System.Windows.Forms"
psFile.WriteLine "Add-Type -AssemblyName System.Drawing"
psFile.WriteLine ""
psFile.WriteLine "$form = New-Object System.Windows.Forms.Form"
psFile.WriteLine "$form.Text = 'LOCIF Gestion - Chargement...'"
psFile.WriteLine "$form.Size = New-Object System.Drawing.Size(400,300)"
psFile.WriteLine "$form.StartPosition = 'CenterScreen'"
psFile.WriteLine "$form.FormBorderStyle = 'None'"
psFile.WriteLine "$form.BackColor = 'White'"
psFile.WriteLine "$form.TopMost = $true"
psFile.WriteLine ""
psFile.WriteLine "$pictureBox = New-Object System.Windows.Forms.PictureBox"
psFile.WriteLine "$pictureBox.Size = New-Object System.Drawing.Size(350,250)"
psFile.WriteLine "$pictureBox.Location = New-Object System.Drawing.Point(25,25)"
psFile.WriteLine "$pictureBox.SizeMode = 'Zoom'"
psFile.WriteLine "$pictureBox.Image = [System.Drawing.Image]::FromFile('" & imagePath & "')"
psFile.WriteLine "$form.Controls.Add($pictureBox)"
psFile.WriteLine ""
psFile.WriteLine "$form.Show()"
psFile.WriteLine "$form.Refresh()"
psFile.WriteLine "Start-Sleep -Seconds 3"
psFile.WriteLine "$form.Close()"
psFile.Close

' Lancer la fenêtre de chargement
WshShell.Run "powershell.exe -WindowStyle Hidden -ExecutionPolicy Bypass -File """ & psScript & """", 0, False

' Attendre un peu puis lancer l'application
WScript.Sleep 500
WshShell.Run chr(34) & "LocifGestion.bat" & chr(34), 0

' Attendre que l'application se lance et la mettre au premier plan
WScript.Sleep 4000

' Essayer de mettre la fenêtre Java au premier plan
WshShell.AppActivate "LOCIF"
If Err.Number <> 0 Then
    Err.Clear
    WshShell.AppActivate "Java"
    If Err.Number <> 0 Then
        Err.Clear
        WshShell.AppActivate "JavaFX"
    End If
End If

' Nettoyer le fichier temporaire
fso.DeleteFile psScript

Set WshShell = Nothing
Set fso = Nothing

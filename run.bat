@echo off
title JavaStock - Console
REM %~dp0 = repertoire du script (avec \ final)
set PROJECT_DIR=%~dp0

echo ============================================
echo   JavaStock - Lancement de l'application
echo ============================================
echo.
echo L'application va demarrer dans cette fenetre.
echo Pour arreter : fermez cette fenetre ou appuyez sur CTRL+C.
echo.

REM --- Nettoyage des anciens processus JavaStock orphelins ---
for /f "tokens=2" %%a in ('tasklist /FI "WINDOWTITLE eq JavaStock*" /FO LIST 2^>nul ^| findstr "PID:"') do (
    taskkill /PID %%a /F >nul 2>&1
)

REM --- Lancement en premier plan avec java (pas javaw) ---
REM   Le processus est lie a cette console : fermer la console = tuer le processus
echo Demarrage de JavaStock...
echo.
java -Dfile.encoding=UTF-8 -cp "%PROJECT_DIR%lib\postgresql-42.7.1.jar;%PROJECT_DIR%bin\JavaStocks" JavaStocks.MainMenu

REM --- Nettoyage apres fermeture ---
echo.
echo JavaStock s'est arrete.
echo Nettoyage des processus restants...
taskkill /F /IM java.exe /FI "WINDOWTITLE eq JavaStock*" >nul 2>&1
taskkill /F /IM javaw.exe /FI "WINDOWTITLE eq JavaStock*" >nul 2>&1
echo Nettoyage termine.
pause

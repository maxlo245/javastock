@echo off
title JavaStock - Test Base de Donnees
REM %~dp0 = repertoire du script (avec \ final)
set PROJECT_DIR=%~dp0
set CP=%PROJECT_DIR%lib\postgresql-42.7.1.jar;%PROJECT_DIR%src

echo ============================================
echo   JavaStock - Test Base de Donnees
echo ============================================
echo.
echo Pour arreter : fermez cette fenetre ou appuyez sur CTRL+C.
echo.

echo Compilation des fichiers Java...
javac -encoding UTF-8 -sourcepath "%PROJECT_DIR%src" -cp "%CP%" -d "%PROJECT_DIR%bin\JavaStocks" "%PROJECT_DIR%src\JavaStocks\MainMenu.java"
if errorlevel 1 (
    echo Erreur de compilation!
    pause
    exit /b 1
)
echo Compilation reussie!
echo.
echo Execution du test de la base de donnees...
echo.
java -Dfile.encoding=UTF-8 -cp "%PROJECT_DIR%lib\postgresql-42.7.1.jar;%PROJECT_DIR%bin\JavaStocks" JavaStocks.TestDatabase

echo.
echo Test termine. Nettoyage des processus...
REM Nettoyage des processus java orphelins lies a JavaStocks
for /f "tokens=2 delims=," %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO CSV /NH 2^>nul ^| findstr /I "java"') do (
    wmic process where "ProcessId=%%~a" get CommandLine 2>nul | findstr /I "JavaStocks" >nul && taskkill /PID %%~a /F >nul 2>&1
)
echo Nettoyage termine.
pause

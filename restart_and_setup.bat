@echo off
REM Script pour redemarrer PostgreSQL et initialiser la base de donnees
REM Ce script demande les droits administrateur

echo.
echo ========================================
echo Redemarrage de PostgreSQL...
echo ========================================
echo.

REM Check if running as admin
fsutil dirty query C: >nul
if %errorlevel% NEQ 0 (
    echo Ce script necessite les droits administrateur.
    echo.
    echo Veuillez redemarrer ce script avec les droits administrateur.
    echo.
    pause
    exit /b 1
)

REM Redemarrer le service PostgreSQL
echo Redemarrage du service PostgreSQL...
net stop "postgresql-x64-16"
timeout /t 2 >nul
net start "postgresql-x64-16"
timeout /t 3 >nul

REM Executer le script d'initialisation
echo.
echo Initialisation de la base de donnees...
echo.
call C:\Users\maxim\IdeaProjects\javastock\setup_database.bat

pause


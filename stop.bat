@echo off
title JavaStock - Arret des processus
echo ============================================
echo   JavaStock - Arret de tous les processus
echo ============================================
echo.
echo Recherche des processus JavaStock en cours...
echo.

set FOUND=0

REM --- Tuer tous les processus java/javaw lies a JavaStocks ---
for /f "tokens=2 delims=," %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO CSV /NH 2^>nul ^| findstr /I "java"') do (
    wmic process where "ProcessId=%%~a" get CommandLine 2>nul | findstr /I "JavaStocks" >nul
    if not errorlevel 1 (
        echo [KILL] Processus java.exe PID=%%~a (JavaStocks)
        taskkill /PID %%~a /F >nul 2>&1
        set FOUND=1
    )
)

for /f "tokens=2 delims=," %%a in ('tasklist /FI "IMAGENAME eq javaw.exe" /FO CSV /NH 2^>nul ^| findstr /I "javaw"') do (
    wmic process where "ProcessId=%%~a" get CommandLine 2>nul | findstr /I "JavaStocks" >nul
    if not errorlevel 1 (
        echo [KILL] Processus javaw.exe PID=%%~a (JavaStocks)
        taskkill /PID %%~a /F >nul 2>&1
        set FOUND=1
    )
)

REM --- Tuer les processus psql orphelins ---
for /f "tokens=2 delims=," %%a in ('tasklist /FI "IMAGENAME eq psql.exe" /FO CSV /NH 2^>nul ^| findstr /I "psql"') do (
    echo [KILL] Processus psql.exe PID=%%~a
    taskkill /PID %%~a /F >nul 2>&1
    set FOUND=1
)

if "%FOUND%"=="0" (
    echo Aucun processus JavaStock trouve en cours d'execution.
) else (
    echo.
    echo Tous les processus ont ete arretes.
)

echo.
echo ============================================
echo   Verification finale
echo ============================================
echo.
echo Processus java restants :
tasklist /FI "IMAGENAME eq java.exe" 2>nul | findstr /I "java" || echo   Aucun
echo.
echo Processus javaw restants :
tasklist /FI "IMAGENAME eq javaw.exe" 2>nul | findstr /I "javaw" || echo   Aucun
echo.
pause

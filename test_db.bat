@echo off
set JAVA_BIN=C:\Program Files\JetBrains\DataGrip 2024.3\jbr\bin
set PROJECT_DIR=C:\Users\maxim\IdeaProjects\javastock
set CP=%PROJECT_DIR%\lib\postgresql-42.7.1.jar;%PROJECT_DIR%\src
echo Compilation des fichiers Java...
"%JAVA_BIN%\javac.exe" -cp "%CP%" -d "%PROJECT_DIR%\bin\JavaStocks\JavaStocks" "%PROJECT_DIR%\src\JavaStocks\*.java"
if errorlevel 1 (
    echo Erreur de compilation!
    pause
    exit /b 1
)
echo Compilation reussie!
echo.
echo Execution du test de la base de donnees...
echo.
"%JAVA_BIN%\java.exe" -cp "%PROJECT_DIR%\lib\postgresql-42.7.1.jar;%PROJECT_DIR%\bin\JavaStocks\JavaStocks" JavaStocks.TestDatabase
pause

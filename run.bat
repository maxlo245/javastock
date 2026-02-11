@echo off
set JAVA_BIN=C:\Program Files\JetBrains\DataGrip 2024.3\jbr\bin
set PROJECT_DIR=C:\Users\maxim\IdeaProjects\javastock
echo Lancement de JavaStock...
echo.
start "JavaStock" "%JAVA_BIN%\javaw.exe" -cp "%PROJECT_DIR%\lib\postgresql-42.7.1.jar;%PROJECT_DIR%\bin\JavaStocks\JavaStocks" JavaStocks.MainMenu
timeout /t 2 >nul
echo.
echo Application lancee! Verifiez la fenetre JavaStock.
echo.
pause

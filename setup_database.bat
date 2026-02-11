@echo off
REM Script d'initialisation de la base de donnees PostgreSQL pour JavaStock

set POSTGRES_PATH=C:\Program Files\PostgreSQL\16\bin
set PG_USER=postgres
set PG_HOST=127.0.0.1
set PG_PORT=5432
set DB_NAME=javastock
set DB_USER=admin
set DB_PASSWORD=root
set SCHEMA_FILE=C:\Users\maxim\IdeaProjects\javastock\db\schema.sql

echo.
echo ============================================
echo Setup PostgreSQL Database for JavaStock
echo ============================================
echo.

REM Create temporary SQL file for user and database creation
set TEMP_SQL=%TEMP%\create_db_%RANDOM%.sql

(
echo DO $$
echo BEGIN
echo     IF NOT EXISTS ^(SELECT 1 FROM pg_roles WHERE rolname = '%DB_USER%'^) THEN
echo         CREATE ROLE %DB_USER% WITH LOGIN PASSWORD '%DB_PASSWORD%';
echo     END IF;
echo END
echo $$;
echo.
echo SELECT format^('CREATE DATABASE %%I OWNER %%I', '%DB_NAME%', '%DB_USER%'^) WHERE NOT EXISTS ^(SELECT 1 FROM pg_database WHERE datname = '%DB_NAME%'^);
echo \gexec
echo.
echo GRANT ALL PRIVILEGES ON DATABASE %DB_NAME% TO %DB_USER%;
) > "%TEMP_SQL%"

echo Creating database and user...
"%POSTGRES_PATH%\psql.exe" -v ON_ERROR_STOP=1 -U %PG_USER% -h %PG_HOST% -p %PG_PORT% -f "%TEMP_SQL%"
if errorlevel 1 (
    echo Error creating database
    del "%TEMP_SQL%"
    pause
    exit /b 1
)
del "%TEMP_SQL%"

REM Initialize schema
echo.
echo Initializing database schema...
set PGPASSWORD=%DB_PASSWORD%
"%POSTGRES_PATH%\psql.exe" -v ON_ERROR_STOP=1 -U %DB_USER% -h %PG_HOST% -p %PG_PORT% -d %DB_NAME% -f "%SCHEMA_FILE%"

REM Clear password variable
set PGPASSWORD=

echo.
echo ============================================
echo PostgreSQL Setup Complete!
echo ============================================
echo Database: %DB_NAME%
echo User: %DB_USER%
echo Password: %DB_PASSWORD%
echo Host: %PG_HOST%
echo Port: %PG_PORT%
echo ============================================
echo.
pause

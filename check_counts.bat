@echo off
set POSTGRES_PATH=C:\Program Files\PostgreSQL\16\bin
set PG_USER=admin
set PG_HOST=127.0.0.1
set PG_PORT=5432
set DB_NAME=javastock

"%POSTGRES_PATH%\psql.exe" -U %PG_USER% -h %PG_HOST% -p %PG_PORT% -d %DB_NAME% -c "SELECT 'article' AS table, COUNT(*) FROM article UNION ALL SELECT 'coureur', COUNT(*) FROM coureur UNION ALL SELECT 'type_epreuve', COUNT(*) FROM type_epreuve UNION ALL SELECT 'reservation', COUNT(*) FROM reservation UNION ALL SELECT 'reservation_article', COUNT(*) FROM reservation_article;"
pause


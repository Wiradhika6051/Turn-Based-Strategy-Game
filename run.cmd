@echo off
REM Change directory to the project root if needed
cd /d "%~dp0"

REM Run the JavaFX app
.\target\tbsg-app\bin\tbsg

REM Pause so you can see errors
pause

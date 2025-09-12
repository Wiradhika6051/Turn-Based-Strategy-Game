@echo off
REM Change directory to the project root if needed
cd /d "%~dp0"
REM Check if first argument is "skip"
if "%~1"=="skip" (
    echo Skipping build...
) else (
    REM Build the project
    call .\mvnw.cmd clean javafx:jlink
    if errorlevel 1 (
        echo Build failed. Exiting...
        pause
        exit /b %errorlevel%
    )
)
REM Run the JavaFX app
.\target\tbsg-app\bin\tbsg

REM Pause so you can see errors
pause

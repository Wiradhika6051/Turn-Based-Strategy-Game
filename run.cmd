@echo off
cd /d "%~dp0"

rem Build if not skip
if "%~1"=="skip" (
    echo Skipping build...
) else (
    call .\mvnw.cmd clean javafx:jlink
    if errorlevel 1 (
        echo Build failed. Exiting...
        pause
        exit /b %errorlevel%
    )
)

rem Run app and capture all output
.\target\tbsg-app\bin\tbsg > crashlog.txt 2>&1

echo.
echo Application exited. See crashlog.txt for details.
pause



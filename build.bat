@echo off
REM Miracle Hospital Management System - Build and Run Script for Windows

setlocal enabledelayedexpansion

echo ================================
echo Miracle Hospital - Java App
echo ================================
echo.

REM Check Java
echo Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo X Java is not installed
    exit /b 1
)
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| find "version"') do set JAVA_VERSION=%%i
echo OK Java version: %JAVA_VERSION%

REM Check Maven
echo Checking Maven...
mvn -v >nul 2>&1
if errorlevel 1 (
    echo X Maven is not installed
    exit /b 1
)
echo OK Maven installed

REM Parse command
set COMMAND=%1
if "%COMMAND%"=="" set COMMAND=build

if "%COMMAND%"=="build" (
    echo.
    echo Building project...
    call mvn clean install
    if errorlevel 1 exit /b 1
    echo OK Build completed
) else if "%COMMAND%"=="run" (
    echo.
    echo Running application...
    call mvn spring-boot:run
) else if "%COMMAND%"=="test" (
    echo.
    echo Running tests...
    call mvn test
) else if "%COMMAND%"=="docker-build" (
    echo.
    echo Building Docker image...
    docker build -t miracle-hospital:latest .
    echo OK Docker image built
) else if "%COMMAND%"=="docker-run" (
    echo.
    echo Running Docker containers...
    docker-compose up -d
    echo OK Containers started
    echo Application available at: http://localhost:8080
) else if "%COMMAND%"=="docker-stop" (
    echo.
    echo Stopping Docker containers...
    docker-compose down
    echo OK Containers stopped
) else (
    echo Usage: build.bat {build^|run^|test^|docker-build^|docker-run^|docker-stop}
    echo.
    echo Commands:
    echo   build         - Build the Maven project
    echo   run           - Run the Spring Boot application
    echo   test          - Run unit tests
    echo   docker-build  - Build Docker image
    echo   docker-run    - Run Docker containers (docker-compose)
    echo   docker-stop   - Stop Docker containers
    exit /b 1
)

echo.
echo OK Done!
endlocal

#!/bin/bash

# Miracle Hospital Management System - Build and Run Script

set -e

echo "================================"
echo "Miracle Hospital - Java App"
echo "================================"
echo ""

# Check Java version
echo "Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | grep -oE 'version "[^"]*' | cut -d' ' -f2 | cut -d'"' -f2)
echo "✓ Java version: $JAVA_VERSION"

# Check Maven
echo ""
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed"
    exit 1
fi

MVN_VERSION=$(mvn -v | grep "Apache Maven" | awk '{print $3}')
echo "✓ Maven version: $MVN_VERSION"

# Parse command
COMMAND=${1:-build}

case $COMMAND in
    build)
        echo ""
        echo "Building project..."
        mvn clean install
        echo "✓ Build completed successfully"
        ;;
    run)
        echo ""
        echo "Running application..."
        mvn spring-boot:run
        ;;
    test)
        echo ""
        echo "Running tests..."
        mvn test
        ;;
    docker-build)
        echo ""
        echo "Building Docker image..."
        docker build -t miracle-hospital:latest .
        echo "✓ Docker image built"
        ;;
    docker-run)
        echo ""
        echo "Running Docker containers..."
        docker-compose up -d
        echo "✓ Containers started"
        echo "Application available at: http://localhost:8080"
        ;;
    docker-stop)
        echo ""
        echo "Stopping Docker containers..."
        docker-compose down
        echo "✓ Containers stopped"
        ;;
    *)
        echo "Usage: $0 {build|run|test|docker-build|docker-run|docker-stop}"
        echo ""
        echo "Commands:"
        echo "  build         - Build the Maven project"
        echo "  run           - Run the Spring Boot application"
        echo "  test          - Run unit tests"
        echo "  docker-build  - Build Docker image"
        echo "  docker-run    - Run Docker containers (docker-compose)"
        echo "  docker-stop   - Stop Docker containers"
        exit 1
        ;;
esac

echo ""
echo "✓ Done!"

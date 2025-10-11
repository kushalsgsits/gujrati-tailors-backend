#!/bin/bash

echo "🚀 Deploying to Google App Engine..."
echo ""

APP_YAML="src/main/appengine/app.yaml"

# Check if JWT_SECRET needs to be set
if grep -q "REPLACE_WITH_YOUR_SECURE_PRODUCTION_JWT_SECRET" "$APP_YAML"; then
    echo "🔐 JWT_SECRET not configured in app.yaml"
    echo ""
    echo "Choose an option:"
    echo "  1) Generate a new random secret (recommended)"
    echo "  2) Enter your own secret (min 64 characters)"
    echo ""
    read -p "Enter choice (1 or 2): " choice
    echo ""

    if [ "$choice" = "1" ]; then
        echo "Generating secure JWT secret..."
        JWT_SECRET=$(openssl rand -base64 64 | tr -d '\n' | tr -d '%')
        echo "✅ Secret generated!"
    elif [ "$choice" = "2" ]; then
        read -p "Enter JWT secret (min 64 chars): " JWT_SECRET
        if [ ${#JWT_SECRET} -lt 64 ]; then
            echo "❌ Secret must be at least 64 characters long!"
            exit 1
        fi
    else
        echo "❌ Invalid choice!"
        exit 1
    fi

    # Update app.yaml with the secret
    echo ""
    echo "Updating app.yaml with JWT_SECRET..."
    sed -i.bak "s|REPLACE_WITH_YOUR_SECURE_PRODUCTION_JWT_SECRET_AT_LEAST_64_CHARACTERS_LONG_RANDOMLY_GENERATED|$JWT_SECRET|g" "$APP_YAML"
    echo "✅ app.yaml updated!"
else
    echo "✅ JWT_SECRET already configured in app.yaml"
fi

# Build the application
echo ""
echo "📦 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Deploy to App Engine
echo ""
echo "☁️  Deploying to App Engine..."
mvn appengine:deploy

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Deployment successful!"
    echo ""
    echo "🌐 Your app is live at: https://gujrati-tailors-backend.el.r.appspot.com"
    echo ""
    echo "📋 View logs: gcloud app logs tail -s default"
else
    echo "❌ Deployment failed!"
    exit 1
fi


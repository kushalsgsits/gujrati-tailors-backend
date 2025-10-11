#!/bin/bash

# Environment variables for Google Datastore Emulator
export DATASTORE_DATASET=gujrati-tailors-backend
export DATASTORE_EMULATOR_HOST=localhost:8081
export DATASTORE_EMULATOR_HOST_PATH=localhost:8081/datastore
export DATASTORE_HOST=http://localhost:8081
export DATASTORE_PROJECT_ID=gujrati-tailors-backend

echo "Environment variables set for Datastore Emulator:"
echo "  DATASTORE_DATASET=$DATASTORE_DATASET"
echo "  DATASTORE_EMULATOR_HOST=$DATASTORE_EMULATOR_HOST"
echo "  DATASTORE_EMULATOR_HOST_PATH=$DATASTORE_EMULATOR_HOST_PATH"
echo "  DATASTORE_HOST=$DATASTORE_HOST"
echo "  DATASTORE_PROJECT_ID=$DATASTORE_PROJECT_ID"
echo ""
echo "To run the application with these variables:"
echo "  source set_vars.sh && mvn spring-boot:run"

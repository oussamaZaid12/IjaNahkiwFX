#!/bin/sh

echo 'Creating Python virtual environment "scripts/.venv"'
python3 -m venv ./scripts/.venv

echo 'Installing dependencies from "requirements.txt" into virtual environment'
./scripts/.venv/bin/python -m pip install -r ./requirements.txt

./scripts/.venv/bin/python setup.py -s $AZURE_SUBSCRIPTION_ID -r $AZURE_ORCHESTRATOR_FUNC_RG -f $AZURE_ORCHESTRATOR_FUNC_NAME -k $AZURE_KEY_VAULT_NAME

# tests/test_main.py
import os
import sys
from fastapi.testclient import TestClient

# Adjust the path to include the app directory for importing main.py
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../app')))

# Import the FastAPI app from main.py
from main import app  # Ensure this line works now if the path is set correctly

# Create a TestClient for the FastAPI app
client = TestClient(app)

def test_read_root():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"Hello": "World?"}

def test_testing():
    response = client.get("/test")
    assert response.status_code == 200
    assert response.json() == [55]

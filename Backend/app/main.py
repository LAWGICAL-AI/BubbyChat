# app/main.py
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World hiiiiii"}

@app.get("/test")
def testing():
    return {55}

@app.get("/inference")
def testing():
    return {"return":"inference test"}
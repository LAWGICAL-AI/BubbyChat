# main.py
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World hiiiiii"}

@app.get("/test")
def testing():
    return {"testing FastAPI!"}

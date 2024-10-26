# app/main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 모든 도메인에서 접근 허용
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def read_root():
    return {"Hello": "World?"}

@app.get("/test")
def testing():
    return {"value": 55}

@app.get("/inference")
def inference():
    return {"return": "inference test"}

@app.get("/connection")
def inference():
    return {"return": "yes yes it did worked"}

@app.get("/new")
def inference():
    return {"return": "test to check ci/cd"}
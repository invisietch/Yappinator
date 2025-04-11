FROM python:3.12-slim

WORKDIR /app

COPY model-utils/requirements.txt .
RUN pip install --upgrade pip && pip install -r requirements.txt

COPY model-utils .

EXPOSE 8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
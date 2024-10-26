# Dockerfile

# Use a Python base image
FROM python:3.10


# Set working directory to /Backend
WORKDIR /Backend

# Copy everything in Backend into the container's /Backend
COPY . /Backend

# Install dependencies
RUN pip install --no-cache-dir -r /Backend/app/requirements.txt

# Install SSH client
RUN apt-get update && apt-get install -y openssh-client

# Set permissions on PEM file
# RUN chmod 400 /Backend/id_rsa.pem

# Expose the FastAPI port (B)
EXPOSE 8001

# Copy entrypoint script and make it executable
COPY entrypoint.sh /Backend/entrypoint.sh
RUN chmod +x /Backend/entrypoint.sh

# Run the entrypoint script
ENTRYPOINT ["/Backend/entrypoint.sh"]
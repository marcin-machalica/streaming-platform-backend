docker run --restart=always -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin1 -p 8180:8180 --name streaming-platform-keycloak -d jboss/keycloak -Djboss.http.port=8180

# ms-products
## Base de Datos

### Iniciar con Docker Compose
```bash
docker-compose up -d
```

### O con comando directo
```bash
docker run -d --name mysql-productos -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=ms-productsdb -e MYSQL_USER=productos_user -e MYSQL_PASSWORD=productos_pass -p 3307:3306 mysql:8.0

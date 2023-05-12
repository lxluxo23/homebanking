<!-- usare esto para guardar comandos que necesite -->
# Docker 
## Comando para crear la imagen 
```
docker build -t homebanking .
```

## Generar el Contenedor con la imagen homebanking y ejecutar en segundo plano
```
docker run -d --name homebanking-container -p 8080:8080 homebanking
```
## Listar imagenes de docker
```
docker image ls
```
## Listar contenedores Docker (running, all, all in quiet mode)
```
docker container ls
docker container ls --all
docker container ls -aq
```
## Para iniciar un contenedor detenido 
```
docker start <nombre_del_contenedor>
docker start homebanking-container

```
## Abrir shell interactiva del contenedor en marcha
```
docker exec -it <nombre_del_contenedor> bash
docker exec -it homebanking-container bash
```
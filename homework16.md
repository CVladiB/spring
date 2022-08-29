**Описание задание:**  
CRUD приложение с Web UI и хранением данных в БД

**Требования:**

1. Система автоматической сборки - Gradle
2. Spring boot 2.7.1 и выше
3. Java 17 и выше
4. БД **PostgreSQL**
5. Создать приложение с хранением сущностей в БД (можно взять библиотеку и DAO/репозитории из прошлых занятий)
6. Использовать классический View на Thymeleaf, classic Controllers.
7. Для книг (главной сущности) на UI должны быть доступные все CRUD операции. CRUD остальных сущностей - по
   желанию/необходимости.
8. Локализацию делать НЕ нужно - она строго опциональна.
9. org.hibernate:hibernate-envers https://docs.jboss.org/envers/docs/

Это домашнее задание частично будет использоваться в дальнейшем

**Полезная информация:**

*Запуск PostgreSQL в docker*  
[hub.docker.com](https://hub.docker.com/_/postgres)

```shell
docker run --rm -d -p 5432:5432 --env POSTGRES_PASSWORD=strong_pass --env POSTGRES_USER=library --name postgres postgres 
```
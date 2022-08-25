**Описание задание:**  
Переписать приложение для хранения книг со слоем репозиториев на Spring Data JPA

**Требования:**

1. Система автоматической сборки - Gradle
2. Spring boot 2.7.1 и выше
3. Java 17 и выше
4. БД **PostgreSQL**
5. Переписать все репозитории по работе с книгами на Spring Data JPA репозитории
6. Используйте spring-boot-starter-data-jpa.
7. Кастомные методы репозиториев (или с хитрым @Query) покрыть тестами, используя H2.
8. @Transactional рекомендуется ставить на методы сервисов, а не репозиториев.
9. Это домашнее задание будет использоваться в качестве основы для других ДЗ

**Полезная информация:**

*Запуск PostgreSQL в docker*  
[hub.docker.com](https://hub.docker.com/_/postgres)

```shell
docker run --rm -d -p 5432:5432 --env POSTGRES_PASSWORD=strong_pass --env POSTGRES_USER=library --name postgres postgres 
```
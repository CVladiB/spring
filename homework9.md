**Описание задание:**  
Переписать приложение для хранения книг на ORM на основании предыдущего домашнего задания

**Требования:**

1. Система автоматической сборки - Gradle
2. Spring boot 2.7.1 и выше
3. Java 17 и выше
4. БД **PostgreSQL**
5. JPA; Hibernate только в качестве JPA-провайдера
6. Для решения проблемы N+1 можно использовать специфические для Hibernate аннотации @Fetch и @BatchSize.
7. Добавить новую сущность "Комментарий к книге" и реализовать CRUD для неё
8. Покрыть репозитории тестами, используя БД H2 и соответствующий H2 Hibernate-диалект для тестов
9. Отключить DDL через Hibernate
10. @Transactional рекомендуется ставить только на методы сервиса  
    Это домашнее задание является основой для следующих

**Полезная информация:**

*Запуск PostgreSQL в docker*  
[hub.docker.com](https://hub.docker.com/_/postgres)
```shell
docker run --rm -d -p 5432:5432 --env POSTGRES_PASSWORD=strong_pass --env POSTGRES_USER=library --name postgres postgres 
```
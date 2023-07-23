# Filtration

При работе со списком сущностей часто возникает ситуация, когда мы хотим просмотреть только те сущности, которые соответствуют определенному критерию. Например, хотим вывести только пользователей с именем 'Alex' или получить список всех инженеров. Для этого используется фильтрация.

## Ссылки
* [Метод like() для проверки соответствия строки паттерну](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/CriteriaBuilder.html#like-javax.persistence.criteria.Expression-javax.persistence.criteria.Expression-)
* [Метод lower() для приведения строки к нижнему регистру](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/CriteriaBuilder.html#lower-javax.persistence.criteria.Expression-)
* [Пример использования QueryDSL](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/)
* [Использование параметров запроса для построения предиката Querydsl](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/)
* [Использование параметров запроса для построения предиката Querydsl и кастомизация](https://spring.io/blog/2015/09/04/what-s-new-in-spring-data-release-gosling#querydsl-web-support)
* [Метод containsIgnoreCase() – проверяет, содержит ли строка переданнную строку без учёта регистра](https://querydsl.com/static/querydsl/4.0.3/apidocs/com/querydsl/core/types/dsl/StringExpression.html#containsIgnoreCase-java.lang.String-)


## src/maim/java/exercise/UserController.java

Один из способов сделать фильтрацию – использовать Spring Data Jpa спецификации

## Задачи

* Создайте в контроллере метод, который обрабатывает GET-запросы по адресу */users*. Метод должен поддерживать фильтрацию пользователей по параметрам `firstName` и `lastName` и возвращать все совпадения по вхождению переданной строки без учета регистра. Параметры для фильтрации передаются в строке запроса. Например запрос на адрес */users?firstName=ale* должен вернуть всех пользователей, чьё имя содержит строку "ale". Используйте спецификации, которые рассмотрели на лекции

```json
[
    {"id":63,"firstName":"Alex","lastName":"Aleksankov", ...},
    {"id":110,"firstName":"Alexandra","lastName":"Ivanova", ...},
    {"id":200,"firstName":"Kaleb","lastName":"Rikson", ...}
]
```

Оба параметра являются необязательными. Если ни один из параметров не передан, метод должен вернуть всех пользователей.


## src/main/java/exercise/service/UserSpecification.java

## Задачи

* В классе `UserSpecification` допишите содержимое метода `toPredicate()`, чтобы фильтрация работала так, как указано выше 

## Дополнительная задача

Минус спецификаций заключается в том, что реализация требует значительных усилий по написанию кода. Эту проблему решает проект под названием Querydsl. Он предлагает несколько иной подход к фильтрации.
Предикаты Querydsl позволяют работать с элементами базы данных как с обычными полями класса. При сборке создаются специальные классы зависимостей с префиксом Q, через которые и происходит поиск нужных записей в БД. Таким образом формируется предикат, а затем из репозитория извлекаются все записи, которые ему удовлетворяют

```java
repository.findAll(QUser.user.address.eq("Edinburgh"));

repository.findAll(
    QUser.user.firstName.eq("Andrey")
        .and(
            QUser.user.gender.eq("Male")
        )
    );
```

Для этого нужно будет несколько модифицировать репозиторий:

```java
public interface UserRepository extends
    JpaRepository<User, Long>,
    QuerydslPredicateExecutor<User>,
    QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings bindings, QUser user) {
        // ...
    }

}

```

* Попробуйте выполнить ту же задачу, используя Querydsl предикаты

Существует еще один, более автоматизированный способ. Spring boot поддерживает [автоматическую генерацию предиката напрямую из параметров строки запроса](https://spring.io/blog/2015/09/04/what-s-new-in-spring-data-release-gosling#querydsl-web-support). Для этого используется аннотация `@QuerydslPredicate`. С её помощью параметры строки запроса автоматически привязываются к соответствующим свойствам сущности. Таким образом, строка запроса *firstname=Dave&lastname=Matthews* автоматически транслируется в Querydsl предикат:

```java
QUser.user.firstname.eq("Dave").and(QUser.user.lastname.eq("Matthews"))
```

По умолчанию для всех полей используется привязка `eq` (равно). Чаще всего же нам нужно использовать особую привязку для каждого свойства. Для этого нужно переопределить значение метода `customize()` в репозитории.

* Сделайте альтернативное решение задачи, используя данную возможность Spring boot. Переопределите метод `customize()` в файле репозитория *src/main/java/exercise/repository/UserRepository.java* так, чтобы для полей `firstName`, `lastName`, `email` и `profession` фильтрация выполнялась по вхождению строки без учета регистра, а для поля `gender` по равенству строк.

* Запустите приложение и попробуйте отправлять ему запросы с различной комбинацией фильтров.

* Допишите тест, который проверит работу приложения при передаче полного набора параметров для фильтрации. Перечень пользователей, которые загружаются в базу при тестировании, можно посмотреть в файле *src/test/resources/dataset/users.yml*

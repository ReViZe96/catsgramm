# первый (вспомогательный) этап с именем builder
FROM amazoncorretto:21-alpine as builder
# устанавливаем application в качестве рабочей директории
WORKDIR application
# копируем артефакт в папку application в контейнере
COPY target/*.jar app.jar
# используем специальный режим запуска Spring Boot приложения,
# который активирует распаковку итогового jar-файла на составляющие
RUN java -Djarmode=layertools -jar app.jar extract

# заключительный этап, создающий финальный образ
FROM amazoncorretto:21-alpine
# поочерёдно копируем необходимые для приложения файлы,
# которые были распакованы из артефакта на предыдущем этапе;
# при этом каждая инструкция COPY создаёт новый слой
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application ./
# в качестве команды указываем запуск специального загрузчика
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

#ОСНОВНЫЕ КОМАНДЫ DOCKER:
# docker build -t <имя_создаваемого_образа> . --- создать образ из Dockerfile, расположенного в данной директории
# docker images --- просмотр списка доступных образов
# docker rmi <хэш_образа> --- удалить образ
# docker create --name <имя_создаваемого_контейнера> <имя_образа> --- создать контейнер с заданным именем из указанного образа
# docker start <хэш_контейнера> --- запустить ранее созданный контейнер
# docker run -d --name <имя_запускаемого_контейнера> -p <порт_внешний>:<порт_в_контейнере> <название_образа> --- запустить
#контейнер на основе образа. docker run заменяет последовательно запущенные docker create и docker start.
# docker ps --- просмотр списка запущенных контейнеров (-a --- просмотр всех контейнеров)
# docker exec -it <имя_запущенного_контейнера> --- подключиться внутрь контейнера
# docker stop <хэш_контейнера> --- остановить контейнер
# docker remove <хэш_контейнера> --- удалить контейнер
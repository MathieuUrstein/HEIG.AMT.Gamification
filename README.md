# HEIG.AMT.Gamification

A gamification platform for aplications that want to attribute badges and points accordingly to rules specified. 

Applications can register to the platform and create rules, badges and point scales for their users. When there is an event notified to the platform related to an user (for example a message posted), his state is modified and accordingly to the rules established, he can gain point or badges. 

This project was made with [Spring Boot](https://projects.spring.io/spring-boot/) in the AMT course at HEIG-VD. 

An example of a gamified application doing requests using the API can be found [there](https://github.com/MathieuUrstein/HEIG.AMT.Gamification.Frontend).

Don't hesitate to check our [wiki](https://github.com/MathieuUrstein/HEIG.AMT.Gamification/wiki) for additionnal information!

# Deployment

To deploy our app, you will need the following:
- Docker 1.13.0
- Docker Compose 1.10.0

*Warning: Before anything, you may need to stop any service running on port 3306 and 8080. You can also define `$GAMIFICATION_APP_PORT` or `$GAMIFICATION_DB_PORT` to redirect one or both ports to your liking.*

1. Clone the repo and cd into it.
2. Copy `deployment/env.sample` to `deployment/env` and edit the file to your liking. Be careful, it will contain your database password !
2. `$ docker-compose up --build db`: this is required the first time you launch the databse, otherwise the app will fail.
3. `$ ctrl + C`, to stop the database once it is setup.
4. `$ docker-compose up --build`
5. That's it, the app should be listening at [http://localhost:8080/](http://localhost:8080/). Of course, 
if you don't run docker directly on your system (for example on a vm), the host should be the address of the docker host and not `localhost`. Moreover if you redefined `$GAMIFICATION_APP_PORT` then you need to adapt the port accordingly.


# Development

If you want to work on the project, you can of course edit files and re-run `deploy.sh` each time, but 
that's a bit long since you have to restart the dockers each time and you can't run the project directly 
from your IDE (because the host in the url of the DB connexion is an alias defined in the docker-compose).

What you could want is run only the mysql docker and run the project in your IDE. In order to do that, 
you will need to do the following:

1. `$ docker-compose up --build db` to run only the container with the DB.
2. You must either:
  * Specify the `MYSQL_USER`, `MYSQL_PASSWORD`, `DATABASE_HOST`, `MYSQL_DATABASE` environment variables before launching the project 
  * Create a configuration file. If you chose the first option, you can stop here. However, the configuration file is still useful to specify hibernate behaviour on startup (see note below).
2. create a [configuration file](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties)
in `/src/main/resources/` named `application-default.properties`. You can name it differently (`application-{profile}.properties`),
but then you will have to specify later the profile used when you run the project.
3. Inside, put the following lines: 
  * `spring.datasource.url=jdbc:mysql://<host>:<port>/<db>?useSSL=false`
  * `spring.datasource.username=<username>`
  * `spring.datasource.password=<password>`
4. You can now run the project from your IDE.

Note: the basic behavior of the server is to keep the values registered in the DB. This can be changed by 
adding the `spring.jpa.hibernate.ddl-auto` parameter in your `application-default.properties` and set it
to wanted behavior (check the [Hibernate documentation](https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl) 
to see the possible values).


# Tests

Unit tests are available to test the application and are run by travis. If you want to run them yourself, here are the steps needed to do it.
Requirements: 
- Python 3.5
- MySQL python client 1.3.9: 
  * libmysqlclient-dev (on linux)
  * [mysqlclient](http://www.lfd.uci.edu/~gohlke/pythonlibs/#mysqlclient) on windows (run `pip install <the downloaded whl>` to install it). Be sure to take `mysqlclient‑1.3.9‑cp35‑cp35m‑*.whl` (win32 or win_amd64 according to your python version)
- You may also want to setup a virtual environment based on python 3 (else, be careful if you have multiple python versions to not use the wrong python or pip).

1. Create a `test.conf` file in the root of the project. You can copy the content of `test_default.conf`.
2. Edit it and specify the correct values:
  * For the mysql DB, the host, port and the password used (if you use the one coming with the docker-compose, set `password = root`).
  * For the gamification app, the host used (by default, `host = localhost`).
3. At the root of the project, run `pip install -r tests/requirements.pip`.
4. Still at the root of the project, run `python -m unittest discover tests`.
5. The results of the unit tests are displayed.

*Note : you can skip running concurrency tests, which take quite some time, by setting the `GAMIFICATION_SKIP_CONCURRENCY_TESTS`
env variable.*


# Authors

Made by [Sébastien Boson](https://github.com/sebastie-boson), 
[Benjamin Schubert](https://github.com/BenjaminSchubert), 
[Mathieu Urstein](https://github.com/MathieuUrstein) and 
[Basile Vu](https://github.com/Flagoul).

# HEIG.AMT.Gamification

A gamification platform for aplications that want to attribute badges and points accordingly to rules specified. 

Applications can register to the platform and create rules, badges and point scales for their users. When there is an event notified to the platform related to an user (for example a message posted), his state is modified and accordingly to the rules established, he can gain point or badges.

Note that this is still a work in progress.

# Deployment

To deploy our app, you will need the following:
- Docker version 1.12.3 and docker-compose
- Apache Maven 3.3.9

Before anything, you may need to stop any service running on port 3306 and 9090.

1. Clone the repo and cd into it
2. `$ ./deploy.sh`
3. That's it, the app should be listening at [http://localhost:9090/](http://localhost:9090/). Of course, if you don't run docker directly
on your system (for example on a vm), the host should be the adress of the docker host and not `localhost`.

If for any reason you prefer to do it manually instead of running the script, you can do the following:
1. `$ mvn package`
2. `$ cp target/gamification-0.0.1-SNAPSHOT.jar images/open-jdk/gamification.jar`
3. `$ docker-compose up --build`

# Development

If you want to work on the project, you can of course edit files and re-run `deploy.sh` each time, but that's a bit long since you have to restart the dockers each time and you can't run the project directly from your IDE (because the host in the url of the DB connexion is an alias defined in the docker-compose).

What you could want is run only the mysql docker and run the project in your IDE. In order to do that, you will need to do the following:

1. `$ docker-compose up --build mysql` to run only the mysql container.
2. create a custom profile in `/src/main/resources/` (for example `application-perso.properties`), and specify the connexion url of the mysql container (you can take the content of `application-default.properties` and replace the `db` part by the correct host).

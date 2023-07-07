# 1st time dev setup (for PC)
## Install
- [Docker](https://hub.docker.com/)
- [nodejs](https://nodejs.org/en) 20.4.0
  - consider [nvm](https://github.com/nvm-sh/nvm) (_node version manager_)
- consider using [sdkman](https://sdkman.io/sdks) to install:
  - [jdk 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
  - [gradle](https://gradle.org/install/) 8.2

## to run 
_the frontend, backend, and mysql db_  
`./gradlew build bootRun`

## whats happening
1. gradle is our build/test/deploy build automation tool. It will check `settings.gradle` and see the **frontend** and **backend** projects
2. each project has a `build.gradle` config file defining how things get built and run
3. **backend**  
   1. has the java configurations for compiling the code and application resources into a java **jar**
   2. has a **plugin** defined that tells it to use **docker-compose** to build a docker container and start up dependencies before it will run
4. **frontend** 
   1. has the config for bundling a single page react app using **react-scripts** which uses webpack for building the artifact
5. the file `compose.yaml`, will bring up a mysql image first, then start the **backend** and **frontend** artifacts
6. it will use the `.env` file for values when starting containers. It currently has values set for a local dev env.
7. liquibase will run as part of spring boots lifecycle 

## frontend details
_from the frontend directory_  
`npm start`
1. uses **react-scripts** to build a debuggable run a development instance of a webpack server
2. todo: introduce storybook for local component development
3. todo: introduce mock http responses for faster iteration on tests
   1. spike: use contracts from java project to generate

## troubleshooting
- sticky DB? Try: 
  - `docker compose down -v mysql-db`
  - `docker volume ls` ensure its gone
- for the freshest of builds (_from project root_)
 ```shell
docker compose down -v mysql-db
.\gradlew build   
docker compose build --no-cache
docker compose up --force-recreate
```
- a little bit faster now
 ```shell
.\gradlew build   
docker compose build
docker compose up

```
# ![https://www.pinterest.com/ryanp926/hype-logo/](./docs/hype.jpg)
_(demo project for testing out new spring libraries)_   
An app that lets you post your hype! What are you hyped about? Tells us now!

### features
- a user signup page and login
- the ability to post a short text about what they're hyped about! (think Twitter)
- an administrator portal that allows someone to view the users and their posted content

### project details
- We're tracking the progress of the project hype prototype [here in trello](https://trello.com/b/yMwWAeQV/hype-prototype)! 
- We're using a [boilerplate generator](https://start.spring.io/) to get things going quickly
- auto-generated [spring help](./HELP.md) docs regarding different capabilties

### how to run locally
- `git clone git@github.com:digiron/hype.git`
- install the dependencies in [dev setup](./docs/dev_setup.md)
- `./gradlew build`
- `docker compose build`
- `docker compose up`
- http://localhost:8080

### dev points of interest
#### Backend
- [Swagger](http://localhost:3000/swagger-ui/index.html)
  - known issue with using cookie auth for jwt tokens (can manually apply using postman)
- [configs](http://localhost:3000/actuator/configprops)
- [env](http://localhost:3000/actuator/env)

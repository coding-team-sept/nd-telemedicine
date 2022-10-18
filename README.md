# RMIT SEPT 2022 Major Project
[![Codemagic build status](https://api.codemagic.io/apps/634e11fc816f0cd621d4d7f5/634e11fc816f0cd621d4d7f4/status_badge.svg)](https://codemagic.io/apps/634e11fc816f0cd621d4d7f5/634e11fc816f0cd621d4d7f4/latest_build)
[![Java CI with Maven](https://github.com/coding-team-sept/nd-telemedicine/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/coding-team-sept/nd-telemedicine/actions/workflows/maven.yml)
# Group Coding Team (Monday - 19.30)

## Members
* Ravel Tanjaya
* Arvin Lee
* Wenlao Peng
* Baiwei Liu
* Naveet Sujith Kumar

## Records

* Github repository : https://github.com/coding-team-sept/nd-telemedicine
* jira Board : https://team-coding.atlassian.net/jira/software/projects/CT/boards/1/roadmap?shared=&atlOrigin=eyJpIjoiODk5YTkyZmRjNDRmNDVlMDg4YWE5M2U2MDY5MmI1Y2IiLCJwIjoiaiJ9
* Google Docs :

## Running the application
### Frontend
```bash
cd FrontEnd
flutter run --release
```

### Backend
```bash
cd BackEnd
docker-compose up -d
```
## Deployment
### Frontend
The frontend needs to be deployed manually as there is we have no developer account associated with either google play and app store,
To automatically generate build artifact build can either be triggered by pushing to the main branch or by triggering the build manually on Codemagic.

### Backend
To deploy the backend, the code can be pushed to the deploy branch or we can just run the action for deploy.


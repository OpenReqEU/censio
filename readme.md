# Project Censio

_This service was created as a result of the OpenReq project funded by the European Union Horizon 2020 Research and Innovation programme under grant agreement No 732463._

## Introduction

Censio is a requirements engineering tool which use several [OpenReq](https://raw.githubusercontent.com/OpenReqEU/) components developed by [SelectionArts](http://www.selectionarts.com/) for the OpenReq Open Call.t s

## Used OpenReq components:

1. [Dependency detection](https://github.com/OpenReqEU/tug-dependency-detection)
2. [Similarity Detection](https://github.com/OpenReqEU/similarity-detection)
3. [Cross reference detection](https://github.com/OpenReqEU/cross-reference-detection)
4. [Improving requirements quality](https://github.com/OpenReqEU/prs-improving-requirements-quality-features)
5. [Gds-edemocracy](https://github.com/OpenReqEU/gds-edemocracy)


## Technical description

Censio is based on Java Spring Boot and use Maven.

### How to install

The project does not require the addition of external dependencies or additional configuration for the base functionallity. It can be built by running:

```
mvn clean install package
```

To use the OpenReq components they must be installed locally.

### How to use

Open http://localhost:8080 in a browser, use mail: test@selectionarts pw: test as login.

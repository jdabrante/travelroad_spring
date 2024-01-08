
<center>

# TÍTULO DE LA PRÁCTICA


</center>

***Nombre:***
***Curso:*** 2º de Ciclo Superior de Desarrollo de Aplicaciones Web.

### ÍNDICE

+ [Introducción](#id1)
+ [Objetivos](#id2)
+ [Material empleado](#id3)
+ [Desarrollo](#id4)
+ [Conclusiones](#id5)


#### ***Introducción***. <a name="id1"></a>

Aquí explicamos brevemente la parte teórica que tiene que ver con la práctica que se va a realizar

#### ***Objetivos***. <a name="id2"></a>

Aquí explicamos los objetivos que se pretenden alcanzar al realizar la práctica.

#### ***Material empleado***. <a name="id3"></a>

Enumeramos el material empleado tanto hardware como software y las conficuraciones que hacemos (configuraciones de red por ejemplo) 

#### ***Desarrollo***. <a name="id4"></a>

##### Servidor de desarrollo

Para la realiazación de este proyecto es necesaria la instalación del JDK ( Java Development Kit ) ya que vamos a trabajar con Java.

Para ello podemos descargar OpenJDK con el siguiente comando:

```
curl -O --output-dir /tmp \
https://download.java.net/java/GA/jdk19.0.1/afdd2e245b014143b62ccb916125e3ce/10/GPL/openjdk-19.0.1_linux-x64_bin.tar.gz
```

Y descomprimimos: 

```
sudo tar -xzvf /tmp/openjdk-19.0.1_linux-x64_bin.tar.gz \
--one-top-level=/usr/lib/jvm
```

Una vez hecho esto realizamos las configuraciones pertinentes del JDK:

```
sudo nano /etc/profile.d/jdk_home.sh
```

Su contenido es el siguiente: 

```
#!/bin/sh
export JAVA_HOME=/usr/lib/jvm/jdk-19.0.1/
export PATH=$JAVA_HOME/bin:$PATH
```

Hecho esto toca actualizar las alternativas para los ejecutables:

```
pc17-dpl@a109pc17dpl:~$ sudo update-alternatives --install \
"/usr/bin/java" "java" "/usr/lib/jvm/jdk-19.0.1/bin/java" 0

pc17-dpl@a109pc17dpl:~$ sudo update-alternatives --install \
"/usr/bin/javac" "javac" "/usr/lib/jvm/jdk-19.0.1/bin/javac" 0

pc17-dpl@a109pc17dpl:~$ sudo update-alternatives --set java \
/usr/lib/jvm/jdk-19.0.1/bin/java

pc17-dpl@a109pc17dpl:~$ sudo update-alternatives --set javac \
/usr/lib/jvm/jdk-19.0.1/bin/javac
```

El siguiente paso en instalar SDKMAN, una herramienta para la gestión de versiones de kit de desarrollo.
Para ello es necesario tener instalado en primer lugar el paquete zip:

```
sudo apt install -y zip
```

Y ejecutamos el scrip de instalación: 

```
curl -s https://get.sdkman.io | bash
```

Y activamos el punto de entrada:

```
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Terminado los preparativos, el siguiente paso es instalar springboot:

```
sdk install springboot
```

Y maven para la gestión de dependencias:

```
sdk install maven
```

El siguiente paso sera la creación del proyecto en si mismo: 

```
spring init \
--build=maven \
--dependencies=web \
--group=edu.dpl \
--name=travelroad \
--description=TravelRoad \
travelroad
```

Hecho esto, configuramos la estrcutura del proyecto para cumplir con el objetivo de la aplicación:

```
pc17-dpl@a109pc17dpl:~/travelroad$ mkdir -p src/main/java/edu/dpl/travelroad/controllers
pc17-dpl@a109pc17dpl:~/travelroad$ touch src/main/java/edu/dpl/travelroad/controllers/HomeController.java

pc17-dpl@a109pc17dpl:~/travelroad$ mkdir -p src/main/java/edu/dpl/travelroad/models
pc17-dpl@a109pc17dpl:~/travelroad$ touch src/main/java/edu/dpl/travelroad/models/Place.java

pc17-dpl@a109pc17dpl:~/travelroad$ mkdir -p src/main/java/edu/dpl/travelroad/repositories
pc17-dpl@a109pc17dpl:~/travelroad$ touch src/main/java/edu/dpl/travelroad/repositories/PlaceRepository.java

pc17-dpl@a109pc17dpl:~/travelroad$ touch src/main/resources/templates/home.html
```

Configuramos el controlador:

```
pc17-dpl@a109pc17dpl:~/travelroad$ vi src/main/java/edu/dpl/travelroad/controllers/HomeController.java
```

```
package edu.dpl.travelroad.controllers;

import edu.dpl.travelroad.models.Place;
import edu.dpl.travelroad.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PlaceRepository placeRepository;

    @Autowired
    public HomeController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("wished", placeRepository.findByVisited(false));
        model.addAttribute("visited", placeRepository.findByVisited(true));
        return "home";  // home.html
    }
}
```

Los modelos: 

```
pc17-dpl@a109pc17dpl:~/travelroad$ vi src/main/java/edu/dpl/travelroad/models/Place.java
```

```
package edu.dpl.travelroad.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Boolean visited;

    public Place() {
    }

    public Place(Long id, String name, Boolean visited) {

        this.id = id;
        this.name = name;
        this.visited = visited;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }
}
```

El repositorio: 

```
pc17-dpl@a109pc17dpl:~/travelroad$ vi src/main/java/edu/dpl/travelroad/repositories/PlaceRepository.java
```

```
package edu.dpl.travelroad.repositories;

import edu.dpl.travelroad.models.Place;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {

    @Query("SELECT p FROM Place p WHERE p.visited = ?1")
    List<Place> findByVisited(Boolean visited);
}
```

Las plantillas ( donde utilizaremos el moto de plantillas Thymeleaf):

```
pc17-dpl@a109pc17dpl:~/travelroad$ vi src/main/resources/templates/home.html
```

```
<!DOCTYPE HTML>
<html>
<head>
    <title>My Travel Bucket List</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <h1>My Travel Bucket List</h1>
    <h2>Places I'd Like to Visit</h2>
    <ul th:each="place : ${wished}">
      <li th:text="${place.name}"></li>
    </ul>

    <h2>Places I've Already Been To</h2>
    <ul th:each="place : ${visited}">
      <li th:text="${place.name}"></li>
    </ul>
</body>
</html>
```

En esta parte explicamos detalladamente los pasos que seguimos para realizar la práctica incluyendo capturas de pantalla y explicando que vemos en ellas. 

> ***IMPORTANTE:*** si estamos capturando una terminal no hace falta capturar todo el escritorio y es importante que se vea el nombre de usuario.

Si encontramos dificultades a la hora de realizar algún paso debemos explicar esas dificultades, que pasos hemos seguido para resolverla y los resultados obtenidos.

#### ***Conclusiones***. <a name="id5"></a>

En esta parte debemos exponer las conclusiones que sacamos del desarrollo de la prácica.

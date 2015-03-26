MazeSolver
==========

Programa generador y solucionador de laberintos utilizando técnicas de IA basadas en el paradigma de agentes.

![Screenshot](prototype/gui.png)

## Descripción
Nuestro objetivo es crear una interfaz gráfica para la creación y visualización de laberintos, la configuración y creación de agentes y la ejecución de simulaciones. Se permite que el usuario tenga un control total sobre todos los aspectos de las simulaciones. Las funcionalidades que proporciona este programa son:
* Creación de laberintos con distintos algoritmos y parámetros.
* Guardado y carga de laberintos en ficheros.
* Visualización simultánea de varios laberintos.
* Ejecución de simulaciones paso a paso y completas simultáneamente en todos los laberintos cargados.
* Adición de distintos tipos de agentes a los laberintos, que serán los que intenten resolverlos.
* Parametrización de los agentes dependiendo de su tipo.
* Realización de "carreras" entre agentes, donde ambos se sitúan en laberintos iguales pero separados y se ejecutan simultáneamente.
* Colocación de varios agentes en un mismo laberinto. Esto hará que haya interacción entre los distintos agentes, bien para colaborar en la búsqueda de la solución o para competir entre sí en un entorno cambiante.
* Visualización de las estadísticas de cada ejecución.

## Recursos que usamos
* [Zulu 8](http://www.azulsystems.com/products/zulu): Es una implementación del JDK basada en OpenJDK libre y multiplataforma, que proporciona las características de la última revisión del lenguaje.
* [Java Swing](http://docs.oracle.com/javase/tutorial/uiswing/): Lo utilizaremos para crear una interfaz gráfica de usuario multiplataforma que permita además el dibujo en bajo nivel de los elementos dentro del laberinto.
* [Algoritmos de creación de laberintos](http://www.astrolog.org/labyrnth/algrithm.htm): En esta página se encuentra una lista amplia de algoritmos de generación de laberintos, en la que nos basamos para implementar los nuestros.
* [ANTLR](http://www.antlr.org): Se trata de un generador de parseadores que puede proporcionar una interfaz para controlar el parser desde Java, además de otros lenguajes. Lo utilizamos para crear un lenguaje de dominio específico para la definición de reglas de situación-acción por el usuario.
* [WebLaF](https://github.com/mgarin/weblaf): Se trata de un Look & Feel diferente al cross-platform de Java que sigue siendo multiplataforma pero con un diseño más elegante.
* [JScroll](http://jscroll.sourceforge.net/index.html): Librería Java que permite el uso de `JDesktopPane` pero añadiendo la función de scroll por defecto, que Java no proporciona por defecto.
* [C10N](https://github.com/rodionmoiseev/c10n): Librería Java que permite el uso de las interfaces de java como elmento de localización, además de dar la opción de usar los resource bundle de java.

## Tecnologías de IA
* **Generación de Laberintos**: Mediante algoritmos aleatorios de generación de árboles en un grafo se podrán generar laberintos perfectos. Además se generarán laberintos no perfectos añadiendo al laberinto perfecto N ciclos o N paredes, separando el laberinto en varias componentes conexas.
* **Búsquedas heurísticas** (Hill-Climbing, A\*, D\*, Simulated annealing): Todos estos algoritmos suponen que el agente conoce la posición de la salida del laberinto para poder hacer una medida heurística de la distancia de cada posición a la salida. Además, el algoritmo A* va a requerir que el agente conozca todo el laberinto de antemano, como si tuviera una visión aérea del mismo.
* **Tablas de percepción-acción**: Mediante tablas de percepción-acción crearemos agentes reactivos simples que se mueven por el laberinto dependiendo de dónde el agente detecta los obstáculos.
* **Reglas de situación-acción**: Se trata de una forma algo más flexible de definir las tablas de percepción-acción, que en nuestro caso además permiten distinguir una mayor variedad de situaciones (distingue otros agentes de paredes, conoce los lugares por los que ha pasado, ...). La arquitectura utilizada para los agentes basados en esto es la de subsunción, donde la regla de mayor prioridad es la primera y la de menor prioridad la última. La regla con mayor prioridad que se cumple define la acción que se toma.
* **Sistemas multiagente**: Como las tablas de percepción-acción y las reglas de situación-acción son muy ineficientes para que un agente pueda encontrar la salida del laberinto por sí mismo, permitimos poner varios agentes en un mismo entorno para que interactúen entre sí. Además podemos crear entornos en los cuales los agentes colaboren entre sí mediante distintos sistemas de comunicación.
* **Comunicación entre agentes**: Mediante el uso de pizarras los agentes se van a poder comunicar entre sí sin importar qué agente sea cada uno, por ejemplo un agente A\* se podría comunicar con un agente de reglas percepción-acción diciendole a éste por dónde el A\* cree que está el camino, aunque algo así no tendría sentido. También se ha implementado un sistema de paso de mensajes entre agentes basado en canales, a pesar de que aún no existen agentes que utilicen esta funcionalidad.

## Desarrollo
El proyecto está desarrollado utilizando [Eclipse Luna](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/lunasr2), por lo que configurar este IDE para desarrollar código para MazeSolver es la opción más sencilla. Los pasos a seguir para importar MazeSolver en el workspace de Eclipse son los siguientes:

1. Haz un [fork de MazeSolver](https://github.com/MazeSolver/MazeSolver/fork).
2. Clona el proyecto: `git clone git@github.com:<tu-usuario>/MazeSolver.git`.
3. Abre Eclipse.
4. Entra en `File > Import`...
5. Bajo el desplegable "General" elige "Existing Projects into Workspace" y pulsa "Next".
6. Pulsa el botón "Browse..." y selecciona el directorio "MazeSolver" creado al clonar el proyecto.
7. Aparecerá el proyecto en la lista "Projects". Selecciónalo si no lo está por defecto y pulsa "Finish".
8. Una vez MazeSolver aparezca en la lista de proyectos, navega hasta `src > es > ull > mazesolver > gui` y abre "MainWindow.java".
9. Ahora se debería poder ejecutar la aplicación pulsando Ctrl+F11.
10. Colabora en el código, crea agentes, tu imaginación es el límite...
11. Registra los cambios y súbelos a GitHub `git add . && git commit -m "Mi contribución a MazeSolver" && git push origin master`.
12. Haz un pull request y tras revisarla la añadiremos al programa para hacerlo disponible a la comunidad.

## [LICENSE](http://www.gnu.org/licenses/gpl-3.0.html) ![LICENSE](http://www.gnu.org/graphics/gplv3-88x31.png)

MazeSolver by its [contributors](https://github.com/kevinrobayna/MazeSolver/graphs/contributors) is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

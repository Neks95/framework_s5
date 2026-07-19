# framework_s5

Développement d'un framework de type **Spring MVC** from scratch.

Projet d'école **S5** – **Mr Naina**.

## Structure des fichiers

### `controller`

* **FrontControllerServlet**

  * Gère les requêtes entrantes.
  * Analyse les URLs.
  * Délègue l'exécution à la méthode de contrôleur correspondante.

### `core`

Contient les principales classes du framework :

* **MethodeControllerMapping**

  * Associe une méthode à sa classe de contrôleur.

* **MethodeHttp**

  * Représente les méthodes HTTP (`GET` ou `POST`).

* **Model**

  * À implémenter.

* **ModelAndView**

  * Contient le nom de la vue ainsi que les attributs à transmettre.

* **UrlMethodeHttpMapping**

  * Associe une URL à une méthode HTTP.

### `itu.annotation`

Contient les annotations du framework :

* `@Controller`
* `@UrlMapping`

### `listener`

Au démarrage de l'application :

* scanne les classes annotées et construit le mapping des URLs ;
* place ce mapping dans le `ServletContext` ;
* démarre le conteneur **Spring** ;
* place l'`ApplicationContext` Spring dans le `ServletContext` ;
* récupère les différents paramètres de configuration (`context-param`) :

  * préfixe des vues ;
  * suffixe des vues ;
  * package des contrôleurs ;
  * package des beans Spring ;
  * etc.

### `utils`

Contient les classes utilitaires du framework :

* exécution des méthodes des contrôleurs ;
* chargement et scan des classes ;
* manipulation des objets `ModelAndView`.

### `view`

* Classe globale contenant les paramètres de configuration des vues :

  * préfixe ;
  * suffixe.

### `exception`

* Ensemble des exceptions propres au framework.

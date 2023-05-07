<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/brunoorsolon/recipewebservice">
    <img src="images/logo.png" alt="Logo" width="150" height="150">
  </a>

  <h3 align="center">Recipes Website</h3>

  <p align="center">
    Unlock the potential of recipe management with our Java-based RESTful API.
    <br />
    <a href="https://github.com/brunoorsolon/recipewebservice/issues">Report Bug</a>
    ·
    <a href="https://github.com/brunoorsolon/recipewebservice/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#tests">Tests</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## About The Project

![product-screenshot]

This project is a Java-based RESTful API for managing and serving recipes, built to support a Mendix-based web application. The motivation behind this project was to create a back-end service that allows web designers to retrieve, filter, and add recipes to the system. The project solves the problem of efficiently organizing and serving recipes through a RESTful API. Throughout the development process, I effectively applied my expertise in API design, Spring Boot, and JAVA while also broadening my knowledge about Mendix integration and its front-end capabilities.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

[![JAVA]][JAVA-url]
[![Spring]][Spring-url]
[![SpringBoot]][SpringBoot-url]
[![SpringMVC]][SpringMVC-url]
[![IntelliJ]][IntelliJ-url]
[![Jacoco]][Jacoco-url]


<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Mendix Studio Pro

### Installation

1. Clone the repository:

   ```
   git clone https://github.com/brunoorsolon/recipewebservice.git
   ```

2. Navigate to the project directory:

   ```
   cd recipewebservice
   ```

3. Build the project using Maven:

   ```
   mvn clean install
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

1. Start the Java RESTful API:

   ```
   java -jar target/recipewebservice-1.0.0.jar
   ```

2. Open Mendix Studio Pro and import the Mendix project provided in the repository.

3. Run the Mendix application locally.

4. Open a browser and navigate to `http://localhost:8081` to access the Mendix web application.

5. After starting the Java RESTful API, the Swagger UI documentation will be available at `http://localhost:8080/swagger-ui.html`. This page, generated with Springdoc, provides an interactive and appUser-friendly interface to explore and test the available API endpoints.

   Available API endpoints:

   - `/api/v1/recipes`: Retrieve, filter, and add recipes.
   - `/api/v1/categories`: Retrieve all available recipe categories.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Maven](https://maven.apache.org/)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Jacoco](https://www.jacoco.org/jacoco/)
- [Mendix](https://www.mendix.com/)
- [Springdoc](https://springdoc.org/)
- [Jackson](https://github.com/FasterXML/jackson)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Project Lombok](https://projectlombok.org/)
- [Jsoup](https://jsoup.org/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Features

- Retrieve recipes with optional filtering by category or keyword
- Retrieve all available recipe categories
- Add new recipes with validation checks
- Integration with Mendix for front-end development

## Tests

1. To run the unit tests, which use JUnit and Mockito for testing and mocking, execute the following Maven command:

  ```
  mvn test
  ```

2. Code coverage reports are generated using Jacoco. After running the tests, you can find the report in the `target/site/jacoco` directory. Open `index.html` in a browser to view the coverage details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

If you have any questions or suggestions, feel free to reach out to me:

[![LinkedIn][linkedin-shield]][linkedin-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/brunoorsolon/recipewebservice.svg?style=for-the-badge
[contributors-url]: https://github.com/brunoorsolon/recipewebservice/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/brunoorsolon/recipewebservice.svg?style=for-the-badge
[forks-url]: https://github.com/brunoorsolon/recipewebservice/network/members
[stars-shield]: https://img.shields.io/github/stars/brunoorsolon/recipewebservice.svg?style=for-the-badge
[stars-url]: https://github.com/brunoorsolon/recipewebservice/stargazers
[issues-shield]: https://img.shields.io/github/issues/brunoorsolon/recipewebservice.svg?style=for-the-badge
[issues-url]: https://github.com/brunoorsolon/recipewebservice/issues
[follow-shield]: https://img.shields.io/github/followers/brunoorsolon.svg?style=social&label=Follow&maxAge=2592000
[follow-url]: https://github.com/brunoorsolon/
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/bruno-orsolon
[product-screenshot]: images/screenshot.jpg
[IntelliJ]: https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
[IntelliJ-url]: https://www.jetbrains.com/idea/
[JAVA]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[JAVA-url]: https://www.java.com/
[Spring]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/
[Jacoco]: https://img.shields.io/badge/Jacoco-5A3F93?style=for-the-badge&logo=jacoco&logoColor=white 
[Jacoco-url]: https://www.jacoco.org/jacoco/
[SpringBoot]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot
[SpringMVC]: https://img.shields.io/badge/Spring_MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[SpringMVC-url]: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html

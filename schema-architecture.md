Section 1: Architecture Summary

This Spring Boot application follows a hybrid architecture that combines both MVC and REST-based components. The MVC structure is used for the Admin and Doctor dashboards, 
which are rendered through Thymeleaf templates to provide dynamic server-side HTML pages. 
In contrast, the rest of the modules—such as patient operations, appointments, and prescriptions—expose RESTful APIs that serve JSON data to clients.
The application communicates with two separate databases: MySQL, which stores relational data such as patients, doctors, appointments, and admin records, and MongoDB, 
which stores non-relational prescription documents. All incoming requests, whether from MVC controllers or REST controllers, 
are passed to a centralized service layer responsible for coordinating business logic. 
The service layer interacts with MySQL via JPA repositories and with MongoDB via Spring Data document repositories. 
This separation ensures clean code organization, reusability of business logic, and consistent data handling throughout the system.

Section 2: Numbered Flow of Data and Control

1.-The user initiates an action—for example, accessing the Admin Dashboard, Doctor Dashboard, or a REST-based endpoint such as appointments or patient data.

2.-The request is routed either to a Thymeleaf MVC controller (for dashboards) or to a REST controller (for API-based modules).

3.-The controller receives the request and forwards it to the service layer, which contains the application’s business logic.

4.-The service layer determines which database needs to be queried (MySQL for relational data or MongoDB for prescription documents).

5.-Depending on the required operation, the service layer calls the corresponding JPA repository (MySQL) or MongoDB repository (prescriptions).

6.-The repository interacts with the configured database, performs the query or update, and returns the results to the service layer.

7.-he service layer sends the processed data back to the controller, which either renders a Thymeleaf template (MVC flow) or returns a JSON response through the REST API.

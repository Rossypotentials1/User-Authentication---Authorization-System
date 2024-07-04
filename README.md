# Spring Security JWT Application
This Spring Boot application demonstrates the use of JWT (JSON Web Tokens) for authentication and authorization. It includes features for user registration, login, and access control for different roles (Admin and User).

## Table of Contents
Prerequisites
Setup Instructions
Running the Application
API Documentation
Authentication Endpoints
User Endpoints
Admin Endpoints
public Endpoints
Prerequisites
Java 17 or higher
Maven 3.6.3 or higher
MySQL database
Setup Instructions
### Clone the repository:

bash
Copy code
git clone https://github.com/your-repo/springsecurityjwt.git
cd springsecurityjwt
Configure the database:

Update application.properties with your MySQL database configuration.
properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
Install dependencies:

bash
Copy code
mvn clean install
Run the application:

bash
Copy code
mvn spring-boot:run
Running the Application
Once the application is up and running, you can use tools like Postman or Curl to interact with the endpoints.

## API Documentation
Authentication Endpoints
Sign Up
URL: /auth/signup
Method: POST
### Description: Registers a new user.
Request Body:
json
Copy code
{
    "name": "Rose Nne",
    "email": "rose@example.com",
    "password": "password123",
    "role": "USER"
}
Response:
json
Copy code
{
    "name": "Rose Nne",
    "email": "john@example.com",
    "message": "User Saved Successfully"
}
Sign In
URL: /auth/signin
Method: POST
### Description: Authenticates a user and returns a JWT token.
Request Body:
json
Copy code
{
    "email": "rose@example.com",
    "password": "password123"
}
Response:
json
Copy code
{
    "token": "jwt_token",
    "expirationTime": "24Hrs",
    "message": "Successfully Signed In"
}
User Endpoints
View Profile
URL: /user/view-profile
Method: GET
### Description: Returns the profile details of the authenticated user.
Authorization: Bearer {jwt_token} (Role: USER)
Response:
json
Copy code
{
    "name": "Rose Nne",
    "email": "rose@example.com",
    "message": "User details retrieved successfully"
}
Admin Endpoints
Save Product
URL: /admin/saveProduct
Method: POST
### Description: Saves a new product.
Authorization: Bearer {jwt_token} (Role: ADMIN)
Request Body:
json
Copy code
{
    "name": "Product Name"
}
Response:
json
Copy code
{
    "id": 1,
    "productName": "Product Name"
}
Delete User
URL: /admin/deleteUser/{userId}
Method: DELETE
### Description: Deletes a user by ID.
Authorization: Bearer {jwt_token} (Role: ADMIN)
Response:
json
Copy code
{
    "message": "User deleted successfully"
}
Common Endpoints
Get All Products
URL: /public/product
Method: GET
### Description: Retrieves all products.
Response:
json
Copy code
[
    {
        "id": 1,
        "productName": "Product 1"
    },
    {
        "id": 2,
        "productName": "Product 2"
    }
]
Admin and User Access
URL: /admin-user
Method: GET
### Description: Accessible by both Admin and User roles.
Authorization: Bearer {jwt_token} (Roles: ADMIN, USER)
Response:
json
Copy code
{
    "message": "Both Admin and user can access this API"
}


## Project Structure
controller: Contains the REST controllers for handling HTTP requests.
service: Contains the service layer for business logic.
repository: Contains the repository layer for data access.
entity: Contains the entity classes representing the database tables.
config: Contains the security configuration classes.
dto: Contains the Data Transfer Object classes.
utils: Contains utility classes like JwtUtils.
Security Configuration
The security configuration is set up to:

Allow unauthenticated access to /auth/** and /public/**.
Restrict access to /admin/** to users with the ADMIN role.
Restrict access to /user/** to users with the USER role.
Allow access to /admin-user/** to both ADMIN and USER roles.
Use JWT tokens for stateless authentication.

<div style="display: flex; align-items: center; justify-content: center;">
  <img src="img/books.png" alt="" width="50" style="margin-right: 10px;">
  <h1 id="start" style="margin: 0;">Online book store</h1>
</div>


<p>Welcome to the online bookstore management system! This platform offers a user-friendly
experience for both customers and administrators. Users can register, browse available books,
add them to their cart, manage their cart contents, and place orders based on their selections.
Administrators, on the other hand, have convenient tools to handle books, categories, and customer
orders. Security is ensured through robust authentication and authorization mechanisms utilizing JWT tokens.</p>


<p align="center" style="font-size: 1.2em;">
  <a href="#technologies" style="text-decoration: none;">🚀 <strong>Technologies</strong></a> •
  <a href="#getting-started" style="text-decoration: none;">🛠️ <strong>Getting Started</strong></a> •
  <a href="#domain-models" style="text-decoration: none;">📚 <strong>Domain Models</strong></a> •
  <a href="#user-roles" style="text-decoration: none;">👥 <strong>User Roles</strong></a> •
  <a href="#endpoints" style="text-decoration: none;">🔗 <strong>Endpoints</strong></a>
  <a href="#challenges" style="text-decoration: none;">📄 <strong>Challenges</strong></a>
</p>


<h2 id="technologies"> Technologies</h2>
<ul style="list-style: none">
 <li><img src="img/java.png" alt="" width="25" style="position: relative; top: 5px;"> Java 17</li>
 <li><img src="img/spring.png" alt="" width="25" style="position: relative; top: 5px;"> Spring framework <i><small>(boot, data, security)</small></i> </li>
 <li><img src="img/lombok.png" alt="" width="25" style="position: relative; top: 5px;"> Lombok</li>
 <li><img src="img/mapstruct.png" alt="" width="25" style="position: relative; top: 5px;"> MapStruct</li>
 <li><img src="img/mysql.png" alt="" width="25" style="position: relative; top: 5px;"> MySql 8</li>
 <li><img src="img/hibernate.png" alt="" width="25" style="position: relative; top: 5px;"> Hibernate</li>
 <li><img src="img/liquibase.svg" alt="" width="25" style="position: relative; top: 5px;"> Liquibase</li>
 <li><img src="img/docker.png" alt="" width="25" style="position: relative; top: 5px;"> Docker</li>
</ul>


<div style="display: flex; align-items: center;">
    <img src="img/swagger.png" alt="" width="50" style="margin-right: 10px;">
    <h1 id="start" style="margin: 0;">Swagger Documentation</h1>
</div>
<p>
    Swagger is integrated into the project to provide comprehensive API documentation and facilitate testing of endpoints. With Swagger, you can visualize and interact with the API's resources without having any of the implementation logic in place.
</p>
<p>To access the Swagger UI and explore the API documentation:</p>
<ol>
    <li>Once the application is running, navigate to <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a> in your web browser.</li>
    <li>You will see the Swagger UI interface, where you can view all available endpoints, their descriptions, request parameters, and response schemas.</li>
    <li>Use the interactive features of Swagger UI to make requests directly from the browser and observe the responses.</li>
</ol>


<div style="display: flex; align-items: center;">
    <img src="img/start.png" alt="" width="50" style="margin-right: 10px;">
    <h1 id="getting-started" style="margin: 0;">Getting Started</h1>
</div>

<h2 style="font-size: medium;">1. Make sure you have installed</h2>

<ul style="list-style: none">
 <li><img src="img/java.png" alt="" width="25" style="position: relative; top: 5px;"> JDK 17+</li>
 <li><img src="img/docker.png" alt="" width="25" style="position: relative; top: 5px;"> Docker</li>
</ul>

<h2 style="font-size: medium;">2. Clone the Repository</h2>
<pre><code>git clone https://github.com/EduarfF/book-store.git
</code></pre>

<h2 style="font-size: medium;">3. Set Environment Variables</h2>
<p>Create a <code>.env</code> file in the project root directory and populate it with the following environment variables:</p>

<pre><code>MYSQLDB_USER=your_db_user_name
MYSQLDB_ROOT_PASSWORD=your_db_password
MYSQLDB_DATABASE=your_db_name
MYSQLDB_LOCAL_PORT=3306
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=your_jwt_expiration
</code></pre>

<h2 style="font-size: medium;">4. Run the following command to build and start the Docker containers</h2>
<pre><code>docker-compose up --build
</code></pre>

<h2 style="font-size: medium;">5. The application should now be running at http://localhost:&lt;YOUR_PORT_FROM_ENV&gt;</h2>


<div style="font-size: larger;">
    <h2 id="domain-models">🎨 Domain Models</h2>
    <div style="margin-bottom: 20px;">
        <h3>User</h3>
        <p><strong>Attributes:</strong> Contains information about registered users, including authentication details and personal information.</p>
        <p><strong>Roles:</strong> Users are assigned roles, such as admin or simple user, defining their permissions and access levels.</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>Role</h3>
        <p><strong>Purpose:</strong> Represents the role of a user within the system.</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>Book</h3>
        <p><strong>Attributes:</strong> Represents detailed information about a book available in the store, including title, author, price, and category.</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>Category</h3>
        <p><strong>Purpose:</strong> Represents a category to which a book can belong.</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>ShoppingCart</h3>
        <p><strong>Purpose:</strong> Represents a user's shopping cart, capable of containing multiple items (CartItems).</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>CartItem</h3>
        <p><strong>Attributes:</strong> Represents an item in a user's shopping cart, linked to a specific book.</p>
    </div>
    <div style="margin-bottom: 20px;">
        <h3>Order</h3>
        <p><strong>Purpose:</strong> Represents an order placed by a user.</p>
    </div>
    <div>
        <h3>OrderItem</h3>
        <p><strong>Attributes:</strong> Represents an item in a user's order, associated with a specific book.</p> 
    </div>
</div>


<div style="display: flex; align-items: center;">
    <img src="img/role.png" alt="" width="50" style="margin-right: 10px;">
    <h1 id="user-roles" style="margin: 0;">User Roles</h1>
</div>

<h3>Shopper (User)</h3>
<p><strong>Actions:</strong></p>
<ul>
    <li>Signing in</li>
    <li>Exploring books</li>
    <li>Searching</li>
    <li>Managing the shopping cart</li>
    <li>Placing and reviewing orders</li>
</ul>

<h3>Manager (Admin)</h3>
<p><strong>Actions:</strong></p>
<ul>
    <li>Managing books</li>
    <li>Managing receipts</li>
    <li>Modifying their status</li>
</ul>


<div style="display: flex; align-items: center;">
    <img src="img/endpoint.png" alt="" width="50" style="margin-right: 10px;">
    <h1 id="endpoints" style="margin: 0;">Endpoints</h1>
</div>

#### Authorization

| **HTTP method** | **Endpoint**             | **Role** | **Description**                   |
|:----------------|:-------------------------|----------|:----------------------------------|
| POST            | /api/auth/authentication |          | Register a new user to the system |
| POST            | /api/auth/login          |          | Login with email and password     |

#### Book management

| **HTTP method** | **Endpoint**  | **Role** | **Description**                 |
|:----------------|:--------------|----------|:--------------------------------|
| GET             | /books        | USER     | Get all books                   |
| GET             | /books/{id}   | USER     | Get the book by id number       |
| GET             | /books/search | USER     | Search books by title or author |
| POST            | /books        | ADMIN    | Create a new book               |
| PUT             | /books/{id}   | ADMIN    | Update book by id number        |
| DELETE          | /books/{id}   | ADMIN    | Delete book by id number        |

#### Categories management

| **HTTP method** | **Endpoint**           | **Role** | **Description**                         |
|:----------------|:-----------------------|----------|:----------------------------------------|
| GET             | /categories            | USER     | Get all categories                      |
| GET             | /categories/{id}       | USER     | Get category by id number               |
| GET             | /categories/{id}/books | USER     | Get list of books by category id number |
| POST            | /categories            | ADMIN    | Create a new category                   |
| PUT             | /categories/{id}       | ADMIN    | Update category by id number            |
| DELETE          | /categories/{id}       | ADMIN    | Delete category by id number            |

#### Shopping cart management

| **HTTP method** | **Endpoint**          | **Role** | **Description**                          |
|:----------------|:----------------------|----------|:-----------------------------------------|
| GET             | /cart                 | USER     | Get shopping cart                        |
| POST            | /cart                 | USER     | Add a new book to shopping cart          |
| PUT             | /cart/cart-items/{id} | USER     | Update quantity of book in shopping cart |
| DELETE          | /cart/cart-items/{id} | USER     | Delete book from shopping cart by id     |

#### Order management

| **HTTP method** | **Endpoint**                | **Role** | **Description**                            |
|:----------------|:----------------------------|----------|:-------------------------------------------|
| POST            | /orders                     | USER     | Place an order based on your shopping cart |
| GET             | /orders                     | USER     | Get all user orders                        |
| GET             | /orders/{id}/items          | USER     | Get all order items by order id            |
| GET             | /orders/{id}/items/{itemId} | USER     | Get order item by order id and item id     |
| PATCH           | /orders/{id}                | ADMIN    | Update order status for order by id        |

<div style="display: flex; align-items: center;">
    <h1 id="challenges" style="margin: 0;">📄Challenges and Solutions</h1>
</div>

<div style="font-size: larger;">
    <div style="margin-bottom: 15px;">
        <h3>Securing the API</h3>
        <p><strong>Solution:</strong> Implemented Spring Security for robust authentication and authorization, safeguarding sensitive endpoints.</p>
    </div>
    <div style="margin-bottom: 15px;">
        <h3>Database Management</h3>
        <p><strong>Solution:</strong> Utilized Spring Data JPA and Hibernate for streamlined database interactions and ORM capabilities.</p>
    </div>
    <div style="margin-bottom: 15px;">
        <h3>API Documentation</h3>
        <p><strong>Solution:</strong> Integrated Swagger for detailed API documentation and simplified endpoint testing.</p>
    </div>
    <div>
        <h3>Database Migrations</h3>
        <p><strong>Solution:</strong> Employed Liquibase for seamless database migrations, ensuring consistency across environments.</p>
    </div>
</div>

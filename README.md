# 📦 Inventory Management System (Web-Based)

A Java-based **web application** built using **Spring MVC**, connected to a **MySQL database** via **JDBC**, and deployed on **Apache Tomcat**. It helps businesses manage products, suppliers, batches, and purchase history. Developed using **Visual Studio Code**.

---

## 🔧 Key Features

- ✅ Product Management
- ✅ Batch and Category Handling
- ✅ Supplier and Product Line Management
- ✅ Purchase History Tracking
- ✅ MVC Architecture (Model, View, Controller)
- ✅ MySQL Integration with JDBC

---

## 🛠️ Technologies Used

| Component         | Technology             |
|------------------|------------------------|
| **Frontend**      | JSP (in `view/` folder)|
| **Backend**       | Java, Spring MVC       |
| **Database**      | MySQL                  |
| **DB Access**     | JDBC                   |
| **Server**        | Apache Tomcat          |
| **IDE**           | Visual Studio Code     |

---

## 📁 Project Structure

src/
├── controller/ # Handles user requests (Spring Controllers)
├── model/ # Java classes: Product, Batch, Supplier, etc.
├── db/ # DBConnection.java (JDBC logic)
├── view/ # JSP files for UI (product.jsp, category.jsp, etc.)
└── Main.java # Entry point if applicable



---

## 🚀 How to Run the Project

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/inventory-management-system.git
   cd inventory-management-system

2. **Set Up MySQL Database**

- MySQL is used as the database.
- You can find the database creation script in the `sql/inventory_db.sql` file.
- Update `DBConnection.java` with your MySQL credentials.


3. **Configure DB Connection**

In DBConnection.java, update the credentials:

String url = "jdbc:mysql://localhost:3306/inventory_db";
String username = "your_db_user";
String password = "your_db_password";

4. **Deploy on Tomcat**

Package and deploy your app using Visual Studio Code with Tomcat support

Access via: http://localhost:8080/your-app-name


Author
Sandra
https://github.com/Sandy-me





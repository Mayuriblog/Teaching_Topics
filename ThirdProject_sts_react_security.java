✅ PART 1: BACKEND (Spring Boot in STS)
🔹 Step 1: Create Spring Boot Project in STS

Go to:

File → New → Spring Starter Project
Project Details:

Project Name: user-management

Type: Maven

Packaging: Jar

Java: 17

Group: com.example

Artifact: usermanagement

Dependencies:

✔ Spring Web
✔ Spring Data JPA
✔ MySQL Driver
✔ Lombok
✔ Spring Security
✔ Spring DevTools

Click Finish.

🔹 Step 2: Configure application.properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/userdb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
🔹 Step 3: Create MySQL Database
CREATE DATABASE userdb;
🔹 Step 4: Create Entity Class

📁 entity/User.java

package com.example.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String about;
	// if lombok is not working case generate getters and setters 
}
🔹 Step 5: Create Repository

📁 repository/UserRepository.java

package com.example.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.usermanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
🔹 Step 6: Create Service

📁 service/UserService.java

package com.example.usermanagement.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User register(User user) {
        return repo.save(user);
    }
	  public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User login(String username, String password) {
        User user = repo.findByUsername(username);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
🔹 Step 7: Create Controller

📁 controller/UserController.java

package com.example.usermanagement.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;
	
      @GetMapping("/")
    public String home() {
        return "Backend Running Successfully";
    }
   
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return service.login(user.getUsername(), user.getPassword());
    }
}
▶ Run Spring Boot Application

Right click → Run As → Spring Boot App
Server runs at:

http://localhost:8080
✅ PART 2: FRONTEND (React)
🔹 Step 1: Create React App

Open terminal:

npx create-react-app snb2user-frontend
cd snb2user-frontend
npm install axios react-router-dom
npm start

🔹 Project Structure
src/
  components/
      Register.js
      Login.js
      Profile.js
  App.js
🔹 Register Component

📁 Register.js

import React, { useState } from "react";
import axios from "axios";

function Register() {

  const [user, setUser] = useState({
    username: "",
    password: "",
    email: "",
    phone: "",
    about: ""
  });

  const handleChange = (e) => {
    setUser({...user, [e.target.name]: e.target.value});
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post("http://localhost:8080/api/register", user)
      .then(res => alert("Registered Successfully"))
      .catch(err => console.log(err));
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register</h2>
      <input name="username" placeholder="Username" onChange={handleChange} /><br/>
      <input name="password" type="password" placeholder="Password" onChange={handleChange} /><br/>
      <input name="email" placeholder="Email" onChange={handleChange} /><br/>
      <input name="phone" placeholder="Phone" onChange={handleChange} /><br/>
      <input name="about" placeholder="About" onChange={handleChange} /><br/>
      <button type="submit">Register</button>
    </form>
  );
}

export default Register;
🔹 Login Component

📁 Login.js

import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Login() {

  const navigate = useNavigate();

  const [loginData, setLoginData] = useState({
    username: "",
    password: ""
  });

  const handleChange = (e) => {
    setLoginData({...loginData, [e.target.name]: e.target.value});
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post("http://localhost:8080/api/login", loginData)
      .then(res => {
        if(res.data){
          localStorage.setItem("user", JSON.stringify(res.data));
          navigate("/profile");
        } else {
          alert("Invalid Credentials");
        }
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input name="username" placeholder="Username" onChange={handleChange} /><br/>
      <input name="password" type="password" placeholder="Password" onChange={handleChange} /><br/>
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;
🔹 Profile Component

📁 Profile.js

import React from "react";

function Profile() {

  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <div>
      <h2>User Profile</h2>
      <p><b>Username:</b> {user.username}</p>
      <p><b>Email:</b> {user.email}</p>
      <p><b>Phone:</b> {user.phone}</p>
      <p><b>About:</b> {user.about}</p>

      <h3>Important Links</h3>
      <a href="https://www.google.com">Google</a><br/>
      <a href="https://github.com">GitHub</a><br/>
      <a href="https://linkedin.com">LinkedIn</a>
    </div>
  );
}

export default Profile;
------------------------------------------------------
🔹 App.js Routing
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Register from "./components/Register";
import Login from "./components/Login";
import Profile from "./components/Profile";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
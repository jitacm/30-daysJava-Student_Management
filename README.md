# **Student Management Application**

A modern Java desktop application for managing student records with a sleek, user-friendly **Graphical User Interface (GUI)**. Built with Java Swing, this app lets you add, view, update, and delete students in an efficient and visually appealing way.


## ⚡ Installation

### 1. Install Java Development Kit (JDK)

- **Required Java Version:** JDK 17 or above (JDK 8+ may work, but JDK 17+ is recommended).
  
- **Check your version:**  
  CMD : java -version
  
- **Download JDK:**  
  [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)  
  [OpenJDK](https://adoptopenjdk.net/)

## 📖 What is This Project?

This Student Management Application is designed to help you manage and organize student information via a robust, easy-to-use GUI. It’s ideal for learning Java GUI development and can serve as a reference or base for educational and small-scale management systems.


## 🚩 How the Application Works

The application enables users to efficiently manage student information through a responsive, interactive interface. Upon launch, you are greeted with a welcoming window featuring navigation buttons on the left and a main table panel for student data.

- Use the sidebar to add new students, view all records, update details, or delete entries.
- Adding or updating opens purposeful dialogs, ensuring data is entered correctly.
- All records are displayed in a structured, scrollable table with alternating row colors for clarity.
- Selection-based actions reduce typing, and changes update instantly in the user interface.
- A status bar at the bottom keeps you informed about operations and their results in real-time.

This design provides a streamlined experience for both casual users and those learning Java GUI best practices.


## ⭐ Features

- **Add Student:**  
  Open an input form to enter details like name, college, city, and percentage. The new student will be added to the system upon submission.

- **Show All Students:**  
  Instantly display all stored student records in a clean, well-formatted table. Zebra striping makes it easy to scan through records quickly.

- **Update Student:**  
  Select any student in the table and edit their data using an intuitive update dialog. All changes are applied immediately and reflected in the main table.

- **Delete Student:**  
  Highlight a student and remove their record with a single click, safeguarded by a confirmation dialog to prevent mistakes.

- **Status Bar for Feedback:**  
  After each action—add, update, delete, or error—the status bar at the bottom provides immediate feedback or guidance to the user.

- **Modern, Intuitive Layout:**  
  Sidebar navigation with labeled, animated buttons.  
  Rounded corners and color feedback on hover for responsive interaction.  
  Input forms include validation for accurate entries and a smooth user experience.

- **Real-time Updates:**  
  All changes are visible as soon as they occur—no need to restart or reload.


## 🔧 Technologies Used

- **Java 17+**
- **Java Swing**  
  (`JFrame`, `JButton`, `JTable`, `JPanel`, `JOptionPane`, and custom components)
- **Object-Oriented Programming** using DAOs for structured data handling
- *No external libraries required*


## 🖥️ Button & Feature Descriptions

- **Add Student:**  
  Opens a dialog where you can input a student’s name, college, city, and percentage.

- **Show All Students:**  
  Shows all student data in a sortable, clean table for easy viewing and browsing.

- **Update Student:**  
  Lets you edit details for any selected student directly from the GUI.

- **Delete Student:**  
  Deletes the selected student after you confirm the action; prevents accidental removal.

- **Status Bar:**  
  Always displays messages about your last action or any errors at the bottom of the window.


## 🛠️ How To Run

Open your terminal (CMD, Powershell, Git Bash, etc.) in the project’s root directory.

**1. Compile all Java source files:**
```
CMD : javac -d out src/dao/*.java src/model/*.java src/gui/*.java src/main/*.java
```
**2. Launch the application:**
```
CMD : java -cp out main.Client
```

## 📁 Project Structure

project-root/
│
├─ src/
│   ├─ dao/         # Data Access Objects
│   ├─ gui/         # GUI code (main interface)
│   ├─ model/       # Student model class
│   └─ main/        # Application entry point
│
├─ out/             # Compiled .class files
├─ README.md
└─ 


## 💡 Additional Notes

- The entire application runs locally and does not require any external databases or internet connection.
- Easily extensible: you can enhance or connect it to databases in the future.
- All data is stored in-memory (ArrayList) for rapid prototyping and demonstration purposes.


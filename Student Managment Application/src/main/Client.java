package main;

import dao.StudentDao;
import dao.StudentDaoInterface;
import model.Student;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        StudentDaoInterface dao = new StudentDao(); // created dao object to access studentDao class methods
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Student Management Application ");

        while (true) {
            System.out.println(
                    "1.Add Student\n2.Show all student\n3.Get student based on roll number\n4.Delete Student\n5.Update Student\n6.Exit");

            System.out.print("Enter choice : ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    System.out.println("Add Student");
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Student clg name: ");
                    String clgName = sc.nextLine();
                    System.out.print("Enter Student city: ");
                    String city = sc.nextLine();
                    System.out.print("Enter Percentage: ");
                    double percentage = Double.parseDouble(sc.nextLine());

                    Student st = new Student(name, clgName, city, percentage);
                    boolean ans = dao.insertStudent(st);
                    if (ans) {
                        System.out.println("Record inserted successfully");
                    } else {
                        System.out.println("Something went wrong, please try again.");
                    }
                    break;

                case 2:
                    System.out.println("Show all student");
                    dao.showAllStudent();
                    break;

                case 3:
                    System.out.print("Enter roll number to search: ");
                    int rollSearch = Integer.parseInt(sc.nextLine());
                    boolean found = dao.showStudentById(rollSearch);
                    if (!found) {
                        System.out.println("Student not found with roll number: " + rollSearch);
                    }
                    break;

                case 4:
                    System.out.print("Enter roll number to delete: ");
                    int rollDelete = Integer.parseInt(sc.nextLine());
                    boolean deleted = dao.delete(rollDelete);
                    if (deleted) {
                        System.out.println("Student deleted.");
                    } else {
                        System.out.println("Student not found with roll number: " + rollDelete);
                    }
                    break;

                case 5:
                    System.out.print("Enter roll number to update: ");
                    int rollUpdate = Integer.parseInt(sc.nextLine());

                    System.out.println("What do you want to update? 1.Name 2.ClgName 3.City 4.Percentage");
                    int updateChoice = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter new value: ");
                    String newValue = sc.nextLine();

                    boolean updated = dao.update(rollUpdate, newValue, updateChoice, null);
                    if (updated) {
                        System.out.println("Student updated.");
                    } else {
                        System.out.println("Student not found or invalid input.");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using Student Management Application.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Please enter valid choice.");
            }
            System.out.println(); // print a blank line for readability before next menu
        }
    }
}

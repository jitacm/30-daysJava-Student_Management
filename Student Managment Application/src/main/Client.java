package main;
import dao.StudentDao;
import dao.StudentDaoInterface;
import model.Student;

import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        StudentDaoInterface dao=new StudentDao(); //created dao object to access studentDao class methods
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Student Management Application ");

        while (true) {
            System.out.println("1.Add Student\n2.Show all student\n3.Get student based on roll number\n4.Delete Student\n5.Update Student\n6.Exit");

            System.out.println("Enter choice : ");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("Add Student");
                    System.out.println("Enter student name");
                    String name=sc.next();
                    System.out.println("Enter Student clg name");
                    String clgName=sc.next();
                    System.out.println("Enter Student city ");
                    String city =sc.next();
                    System.out.println("Enter Percentage");
                    double percentage=sc.nextDouble();
                    //this is parametrized constructor
                    Student st=new Student(name, clgName, city, percentage);
                    boolean ans=dao.insertStudent(st);
                    if(ans){
                        System.out.println("Record inserted successfully");
                    }
                    else{
                        System.out.println("somthing went wrong,please try again ..");
                    }
                    
                    break;  
                case 2:
                    System.out.println("Show all student");
                    break;
                case 3:
                    System.out.println("Get student based on roll number");
                    break;
                case 4:
                    System.out.println("Delete Student");
                    break;
                case 5:
                    System.out.println("Update Student");
                    break;
                case 6:
                    System.out.println("Thank you for using Student Management Application .");
                    System.exit(0);
                    break;
                default:
                System.out.println("Please enter valid choice.");
            }
        }
    }
}


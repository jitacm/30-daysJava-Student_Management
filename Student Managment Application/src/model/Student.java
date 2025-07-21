package model;

public class Student {
    private int rollNum;
    private String name;
    private String clgName;
    private String city;
    private double percentage;
 
 //Default constructor
 Student(){
 
 }
 
 
 // Constructor for all fields
    public Student(int rollNum, String name, String clgName, String city, double percentage) {
     this.rollNum = rollNum;
     this.name = name;
     this.clgName = clgName;
     this.city = city;
     this.percentage = percentage;
 }
 // Constructor excluding rollNum
 public Student(String name, String clgName, String city, double percentage) {
     this.name = name;
     this.clgName = clgName;
     this.city = city;
     this.percentage = percentage;
 }
 //getter and setter for roll number
 public int getRollNum() {
     return rollNum;
 }
 
 public void setRollNum(int rollNum) {
     this.rollNum = rollNum;
 }
 
 // Getter and Setter for name
 public String getName() {
     return name;
 }
 
 public void setName(String name) {
     this.name = name;
 }
 
 // Getter and Setter for clgName
 public String getClgName() {
     return clgName;
 }
 
 public void setClgName(String clgName) {
     this.clgName = clgName;
 }
 
 // Getter and Setter for city
 public String getCity() {
     return city;
 }
 
 public void setCity(String city) {
     this.city = city;
 }
 
 // Getter and Setter for percentage
 public double getPercentage() {
     return percentage;
 }
 
 public void setPercentage(double percentage) {
     this.percentage = percentage;
 }
 
 // toString method to display object details
 @Override
 public String toString() {
     return "Student{" +
             "rollNum=" + rollNum +
             ", name='" + name + '\'' +
             ", clgName='" + clgName + '\'' +
             ", city='" + city + '\'' +
             ", percentage=" + percentage +
             '}';
 }
 }
 
 

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import DB.DBconnection;
import model.Student;

public class StudentDao implements StudentDaoInterface{

    @Override
    public boolean insertStudent(Student s) {
        boolean flag=false;
        try{
        Connection con=DBconnection.createConnection();
        String query="insert into student_details (sname,clgName,city,percentage) value (?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setString(1, s.getName());
        pst.setString(2, s.getClgName());
        pst.setString(3, s.getCity());
        pst.setDouble(4, s.getPercentage());
        pst.executeUpdate();
        flag=true;
    }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean delete(int roll) {
        return false;
    }

    @Override
    public boolean update(int roll, String update, int ch, Student s) {
        return false;
         }

    @Override
    public void showAllStudent() {
        }

    @Override
    public boolean showStudentById(int roll) {
        return false;
        }

    

}


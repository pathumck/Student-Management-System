package lk.ijse.studentmanagementmain.dao.impl;

import lk.ijse.studentmanagementmain.dao.StudentData;
import lk.ijse.studentmanagementmain.dto.StudentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDataProcess implements StudentData {
    static String SAVE_STUDENT = "INSERT INTO student(id,name,city,email,level)VALUE(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM student WHERE id = ?";
    static String DELETE = "DELETE FROM student WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE student SET name =? , city = ?, email =?,level=? WHERE id = ?";
    @Override
    public boolean saveStudent(StudentDTO studentDTO, Connection connection) {
        System.out.println("save student");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STUDENT);
            preparedStatement.setString(1, studentDTO.getId());
            preparedStatement.setString(2, studentDTO.getName());
            preparedStatement.setString(3, studentDTO.getEmail());
            preparedStatement.setString(4, studentDTO.getCity());
            preparedStatement.setString(5, studentDTO.getLevel());

            return preparedStatement.executeUpdate() != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public StudentDTO getStudent(String stuId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT);
        StudentDTO studentDTO = new StudentDTO();
        preparedStatement.setString(1, stuId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            studentDTO.setId(resultSet.getString("id"));
            studentDTO.setName(resultSet.getString("name"));
            studentDTO.setEmail( resultSet.getString("email"));
            studentDTO.setCity( resultSet.getString("city"));
            studentDTO.setLevel( resultSet.getString("level"));
        }
        return studentDTO;
    }

    @Override
    public boolean updateStudent(StudentDTO studentDTO, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT);
            preparedStatement.setString(1,studentDTO.getName());
            preparedStatement.setString(2,studentDTO.getEmail());
            preparedStatement.setString(3,studentDTO.getCity());
            preparedStatement.setString(4,studentDTO.getLevel());
            preparedStatement.setString(5,studentDTO.getId());

            return preparedStatement.executeUpdate() != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStudent(String id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setString(1,id);
            return preparedStatement.executeUpdate() != 0 ;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

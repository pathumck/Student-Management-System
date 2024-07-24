package lk.ijse.studentmanagementmain.dao;

import lk.ijse.studentmanagementmain.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;


public interface StudentData {
    StudentDTO getStudent(String id,Connection connection) throws SQLException;
    boolean updateStudent(StudentDTO studentDTO, Connection connection);
    boolean deleteStudent(String id,Connection connection);
    boolean saveStudent(StudentDTO studentDTO, Connection connection) throws SQLException;
}

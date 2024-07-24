package lk.ijse.studentmanagementmain.controller;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementmain.dao.StudentData;
import lk.ijse.studentmanagementmain.dao.impl.StudentDataProcess;
import lk.ijse.studentmanagementmain.dto.StudentDTO;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")
public class StudentController extends HttpServlet {
    Connection connection;

    /*static String SAVE_STUDENT = "INSERT INTO students(id,name,city,email,level)VALUE(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id = ?";
    static String DELETE = "DELETE FROM students WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE students SET name =? , city = ?, email =?,level=? WHERE id = ?";*/

    StudentData studentData = new StudentDataProcess();

    @Override
    public void init() throws ServletException {
        try {
            var driverclass = getServletContext().getInitParameter("driver-class");
            var dbURL = getServletContext().getInitParameter("dbURL");
            var dbUserName = getServletContext().getInitParameter("dbUserName");
            var dbPassword = getServletContext().getInitParameter("dbPassword");
            //System.out.println(driverclass+dbURL+dbUserName+dbPassword);

            Class.forName(driverclass);
            this.connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("dopost");

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        try {
            String id = UUID.randomUUID().toString();
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);

            studentDTO.setId(id);

            // persist student data
            boolean isSaved = studentData.saveStudent(studentDTO, connection);
            if (isSaved) {
                resp.getWriter().write("Save student");
            } else {
                resp.getWriter().write("Unable to save student");
            }

        /*PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STUDENT);
        for (StudentDTO stu : studentDTO) {
            preparedStatement.setString(1, stu.getId());
            preparedStatement.setString(2, stu.getName());
            preparedStatement.setString(3, stu.getEmail());
            preparedStatement.setString(4, stu.getCity());
            preparedStatement.setString(5, stu.getLevel());

            if (preparedStatement.executeUpdate() != 0) {
                resp.getWriter().write("Save student");
            } else {
                resp.getWriter().write("unable to save student");
            }
        }*/

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String stuId = jsonObject.getString("id");

        try {
            StudentDTO studentDTO = studentData.getStudent(stuId, connection);
            if (studentDTO.getId() == null) {
                resp.getWriter().write("Wrong Id. Please try again.");
            } else {
                resp.getWriter().write(studentDTO.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
//            send error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        String stuId = req.getParameter("id");

        Boolean isDeleted = studentData.deleteStudent(stuId,connection);
        if (isDeleted) {
            resp.getWriter().write(stuId+" : Deleted Successfully!");
        }else {
            resp.getWriter().write("Something wrong! Try again...");
        }

        /*try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setString(1,stuId);
            if (preparedStatement.executeUpdate()>0){
                resp.getWriter().write(stuId+" : Delete successfully!!!");
            }else {
                resp.getWriter().write("Some thing wrong!! Please Try again!!!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
//            send error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        Jsonb jsonb = JsonbBuilder.create();
        StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);

        Boolean isUpdated = studentData.updateStudent(studentDTO,connection);

        if(isUpdated) {
            resp.getWriter().write(studentDTO.getId()+" : Updated Successfully!");
        }else {
            resp.getWriter().write("Something Wrong! Please try again...");
        }

        /*String stuId = jsonObject.getString("id");
        String stuName = jsonObject.getString("name");
        String stuEmail = jsonObject.getString("email");
        String stuCity = jsonObject.getString("city");
        String stuLevel = jsonObject.getString("level");*/
/*
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT);
            preparedStatement.setString(1,stuName);
            preparedStatement.setString(2,stuEmail);
            preparedStatement.setString(3,stuCity);
            preparedStatement.setString(4,stuLevel);
            preparedStatement.setString(5,stuId);

            if (preparedStatement.executeUpdate() != 0) {
                resp.getWriter().write("Update student Successfully !!!");
            } else {
                resp.getWriter().write("unable to Update !! Please try again !!!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

    }
}

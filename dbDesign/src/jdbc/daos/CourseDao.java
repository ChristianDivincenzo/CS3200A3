package jdbc.daos;

import jdbc.models.Course;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


public class CourseDao {
  static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";       // declare JDBC Driver
  static final String HOST = "localhost:3306";                        // declare the HOST
  static final String SCHEMA = "database_design";                     // declare the SCEHMA name
  static final String CONFIG = "serverTimezone=UTC";                  // declare optional configurations
  static final String DB_URL                                          // build connection string
      = "jdbc:mysql://"+HOST+"/"+SCHEMA+"?"+CONFIG;

  static final String USER = "dbDesign";                              // declare username
  static final String PASS = "dbDesign";                              // and password

  static Connection connection = null;                                // connection to connect to database
  static PreparedStatement statement = null;                          // statement to execute SQL
  Integer status = -1;                                                // status returned by some SQL  command

  public static Connection getConnection() {                          // gets connection to database
    try {
      Class.forName(JDBC_DRIVER);                                     // dynamically load JDBC driver
      connection = DriverManager                                      // login and get connection
          .getConnection(DB_URL,USER,PASS);
    } catch (ClassNotFoundException | SQLException e) {               // handle exceptions/errors
      e.printStackTrace();
    }
    return connection;                                                // return connection if all ok
  }


  public static void closeConnection() {
    try {
      if(connection != null) {                                        // if there's a connection
        connection.close();                                           // close it
      }
      if(statement != null) {                                         // if there's a statement
        statement.close();                                            // close it
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  static final String FIND_ALL_COURSES
      = "SELECT * FROM courses";
  public List<Course> findAllCourses() {
    List<Course> courses = new ArrayList<Course>();
    connection = getConnection();
    try {
      statement = connection
          .prepareStatement(FIND_ALL_COURSES);
      ResultSet resultSet = statement.executeQuery();
      while(resultSet.next()) {
        Integer id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        Course course = new Course(id, title);
        courses.add(course);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return courses;
  }


  static final String UPDATE_COURSE =
      "UPDATE courses SET title=? WHERE id=?";
  public Integer updateCourse(Integer courseId, Course course) {
    connection = getConnection();
    try {
      statement = connection.prepareStatement(UPDATE_COURSE);
      statement.setString(1, course.getTitle());
      statement.setInt(2, course.getId());
      status = statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return status;
  }

  static final String FIND_COURSE_BY_ID =
      "SELECT * FROM courses WHERE id=?";
  public Course findCourseById(Integer courseId) {
    connection = getConnection();
    try {
      statement = connection.prepareStatement(FIND_COURSE_BY_ID);
      statement.setInt(1, courseId);
      ResultSet resultSet = statement.executeQuery();
      if(resultSet.next()) {
        String title = resultSet.getString("title");
        Course course = new Course(courseId, title);
        return course;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  static final String CREATE_COURSE
      = "INSERT INTO courses VALUES (?,?)";
  public Integer createCourse(Course course) {
    status = -1;
    connection = getConnection();
    try {
      statement = connection
          .prepareStatement(CREATE_COURSE);
      statement.setInt(1, course.getId());
      statement.setString(2, course.getTitle());
      status = statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return status;
  }




  public static void main(String[] args) {
    CourseDao dao = new CourseDao();
    List<Course> courses = dao.findAllCourses();
    for(Course c:courses) {
      System.out.println(c);
    }

    Course course = new Course(123, "CS2222");
 //   Integer status = dao.updateCourse(123, course);

    courses = dao.findAllCourses();
    for(Course c:courses) {
      System.out.println(c);
    }

    course = dao.findCourseById(123);
    System.out.println(course);

    course = new Course(345, "CS1234");
    Integer status = dao.createCourse(course);
    System.out.println(status);

    course = dao.findCourseById(345);
    System.out.println(course);
  }





}




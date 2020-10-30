
package jdbc.models;

public class Course {
  private Integer id;
  private String title;
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public Course() {    }
  public Course(Integer id, String title) {
    this.id = id;
    this.title = title;
  }
  public String toString() {
    return id + ", " + title;
  }
}
// this class is in package jdbc.models

// this class is called Course
// integer field id to hold record field of the same name and type
// string field title to hold record field of the same name and type
// getter for id field


// setter for the id field


// getter for title


// setter for title


// no argument constructor is required
// all argument constructor added for convenience



// to string method to print instance to console

package vCampus.Entity;

public class Teacher {
    private String id;
    private String course_id;

    // Constructor
    public Teacher(String id, String course_id) {
        this.id = id;
        this.course_id = course_id;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for course_id
    public String getCourseId() {
        return course_id;
    }

    // Setter for course_id
    public void setCourseId(String course_id) {
        this.course_id = course_id;
    }
}

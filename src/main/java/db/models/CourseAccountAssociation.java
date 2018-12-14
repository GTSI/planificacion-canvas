package db.models;

import com.sun.istack.internal.NotNull;

public class CourseAccountAssociation {

  @NotNull
  private long course_id;


  @NotNull
  private long account_id;

  @NotNull
  private long course_section_id;

  public CourseAccountAssociation(long course_id,
                                  long account_id,
                                  long course_section_id) {
    this.course_id = course_id;
    this.account_id = account_id;
    this.course_section_id = course_section_id;
  }

  public long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(long course_id) {
    this.course_id = course_id;
  }

  public long getAccount_id() {
    return account_id;
  }

  public void setAccount_id(long account_id) {
    this.account_id = account_id;
  }

  public long getCourse_section_id() {
    return course_section_id;
  }

  public void setCourse_section_id(long course_section_id) {
    this.course_section_id = course_section_id;
  }
}

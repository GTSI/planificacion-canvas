package db.models;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.sql.Timestamp;

public class CourseSection {

  @Nullable
  private long id;

  @NotNull
  private long course_id;

  @NotNull
  private long root_account_id;

  @NotNull
  private long enrollment_term_id;

  @NotNull
  private String name;

  @NotNull
  private boolean default_section;

  @NotNull
  private Timestamp start_at;

  @NotNull
  private Timestamp end_at;

  public CourseSection(long id,
                       long course_id,
                       long root_account_id,
                       long enrollment_term_id,
                       String name,
                       boolean default_section,
                       Timestamp start_at, Timestamp end_at) {
    this.id = id;
    this.course_id = course_id;
    this.root_account_id = root_account_id;
    this.enrollment_term_id = enrollment_term_id;
    this.name = name;
    this.default_section = default_section;
    this.start_at = start_at;
    this.end_at = end_at;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(long course_id) {
    this.course_id = course_id;
  }

  public long getRoot_account_id() {
    return root_account_id;
  }

  public void setRoot_account_id(long root_account_id) {
    this.root_account_id = root_account_id;
  }

  public long getEnrollment_term_id() {
    return enrollment_term_id;
  }

  public void setEnrollment_term_id(long enrollment_term_id) {
    this.enrollment_term_id = enrollment_term_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isDefault_section() {
    return default_section;
  }

  public void setDefault_section(boolean default_section) {
    this.default_section = default_section;
  }

  public Timestamp getStart_at() {
    return start_at;
  }

  public void setStart_at(Timestamp start_at) {
    this.start_at = start_at;
  }

  public Timestamp getEnd_at() {
    return end_at;
  }

  public void setEnd_at(Timestamp end_at) {
    this.end_at = end_at;
  }
}


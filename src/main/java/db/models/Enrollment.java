package db.models;

public class Enrollment {
  private long id;
  private long user_id;
  private long course_d;
  private String type;
  private String uuid;
  private String workflow_state;
  private long course_section_id;
  private long root_account_id;
  private String grade_publishing_status;
  private boolean limit_privileges_to_course_section;
  private long role_id;
  private boolean from_script;

  public Enrollment(
    long id,
    long user_id,
    long course_d,
    String type,
    String uuid, String workflow_state,
    long course_section_id,
    long root_account_id, String grade_publishing_status,
    boolean limit_privileges_to_course_section,
    long role_id, boolean from_script) {

    this.id = id;
    this.user_id = user_id;
    this.course_d = course_d;
    this.type = type;
    this.uuid = uuid;
    this.workflow_state = workflow_state;
    this.course_section_id = course_section_id;
    this.root_account_id = root_account_id;
    this.grade_publishing_status = grade_publishing_status;
    this.limit_privileges_to_course_section = limit_privileges_to_course_section;
    this.role_id = role_id;
    this.from_script = from_script;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  public long getCourse_d() {
    return course_d;
  }

  public void setCourse_d(long course_d) {
    this.course_d = course_d;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getWorkflow_state() {
    return workflow_state;
  }

  public void setWorkflow_state(String workflow_state) {
    this.workflow_state = workflow_state;
  }

  public long getCourse_section_id() {
    return course_section_id;
  }

  public void setCourse_section_id(long course_section_id) {
    this.course_section_id = course_section_id;
  }

  public long getRoot_account_id() {
    return root_account_id;
  }

  public void setRoot_account_id(long root_account_id) {
    this.root_account_id = root_account_id;
  }

  public String getGrade_publishing_status() {
    return grade_publishing_status;
  }

  public void setGrade_publishing_status(String grade_publishing_status) {
    this.grade_publishing_status = grade_publishing_status;
  }

  public boolean isLimit_privileges_to_course_section() {
    return limit_privileges_to_course_section;
  }

  public void setLimit_privileges_to_course_section(boolean limit_privileges_to_course_section) {
    this.limit_privileges_to_course_section = limit_privileges_to_course_section;
  }

  public long getRole_id() {
    return role_id;
  }

  public void setRole_id(long role_id) {
    this.role_id = role_id;
  }

  public boolean isFrom_script() {
    return from_script;
  }

  public void setFrom_script(boolean from_script) {
    this.from_script = from_script;
  }
}

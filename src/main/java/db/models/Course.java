package db.models;

import org.springframework.lang.Nullable;

import java.sql.Timestamp;

public class Course {

  @Nullable
  private long id;

  private String name;

  private long account_id;

  private String workflow_state;

  @Nullable
  private String syllabus_body;

  private boolean allow_student_forum_attachments;

  private String default_wiki_editing_roles;

  @Nullable
  private long wiki_id;

  private boolean allow_student_organized_groups;

  private String course_code;

  private String default_view;

  private long root_account_id;

  private long enrollment_term_id;

  private String tab_configuration;

  private String sis_teacher_id;

  private Timestamp start_at, conclude_at;

  @Nullable
  private String migration_id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getAccount_id() {
    return account_id;
  }

  public void setAccount_id(long account_id) {
    this.account_id = account_id;
  }

  public String getWorkflow_state() {
    return workflow_state;
  }

  public void setWorkflow_state(String workflow_state) {
    this.workflow_state = workflow_state;
  }

  public String getSyllabus_body() {
    return syllabus_body;
  }

  public void setSyllabus_body(String syllabus_body) {
    this.syllabus_body = syllabus_body;
  }

  public boolean isAllow_student_forum_attachments() {
    return allow_student_forum_attachments;
  }

  public void setAllow_student_forum_attachments(boolean allow_student_forum_attachments) {
    this.allow_student_forum_attachments = allow_student_forum_attachments;
  }

  public String getDefault_wiki_editing_roles() {
    return default_wiki_editing_roles;
  }

  public void setDefault_wiki_editing_roles(String default_wiki_editing_roles) {
    this.default_wiki_editing_roles = default_wiki_editing_roles;
  }

  public long getWiki_id() {
    return wiki_id;
  }

  public void setWiki_id(long wiki_id) {
    this.wiki_id = wiki_id;
  }

  public boolean isAllow_student_organized_groups() {
    return allow_student_organized_groups;
  }

  public void setAllow_student_organized_groups(boolean allow_student_organized_groups) {
    this.allow_student_organized_groups = allow_student_organized_groups;
  }

  public String getCourse_code() {
    return course_code;
  }

  public void setCourse_code(String course_code) {
    this.course_code = course_code;
  }

  public String getDefault_view() {
    return default_view;
  }

  public void setDefault_view(String default_view) {
    this.default_view = default_view;
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

  public String getTab_configuration() {
    return tab_configuration;
  }

  public void setTab_configuration(String tab_configuration) {
    this.tab_configuration = tab_configuration;
  }

  public String getSis_teacher_id() {
    return sis_teacher_id;
  }

  public void setSis_teacher_id(String sis_teacher_id) {
    this.sis_teacher_id = sis_teacher_id;
  }

  public Timestamp getStart_at() {
    return start_at;
  }

  public void setStart_at(Timestamp start_at) {
    this.start_at = start_at;
  }

  public Timestamp getConclude_at() {
    return conclude_at;
  }

  public void setConclude_at(Timestamp conclude_at) {
    this.conclude_at = conclude_at;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMigration_id() {
    return migration_id;
  }

  public void setMigration_id(String migration_id) {
    this.migration_id = migration_id;
  }

  public Course(long id,
                String name,
                long account_id,
                String workflow_state,
                String syllabus_body,
                boolean allow_student_forum_attachments,
                String default_wiki_editing_roles,
                long wiki_id,
                boolean allow_student_organized_groups,
                String course_code, String default_view,
                long root_account_id, long enrollment_term_id,
                String tab_configuration,
                String sis_teacher_id,
                Timestamp start_at,
                Timestamp conclude_at,
                String migration_id) {
    this.id = id;
    this.name = name;
    this.account_id = account_id;
    this.workflow_state = workflow_state;
    this.syllabus_body = syllabus_body;
    this.allow_student_forum_attachments = allow_student_forum_attachments;
    this.default_wiki_editing_roles = default_wiki_editing_roles;
    this.wiki_id = wiki_id;
    this.allow_student_organized_groups = allow_student_organized_groups;
    this.course_code = course_code;
    this.default_view = default_view;
    this.root_account_id = root_account_id;
    this.enrollment_term_id = enrollment_term_id;
    this.tab_configuration = tab_configuration;
    this.sis_teacher_id = sis_teacher_id;
    this.start_at = start_at;
    this.conclude_at = conclude_at;
    this.migration_id = migration_id;
  }
}

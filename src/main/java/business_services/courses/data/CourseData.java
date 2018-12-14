package business_services.courses.data;

public final class CourseData {

  public final int id;
  public final String sis_teacher_id;
  public final String default_view;
  public final String uuid;
  public final int migration_id;
  public final int wiki_id;

  public CourseData(int id,
                    String sis_teacher_id,
                    String default_view,
                    int migration_id,
                    int wiki_id,
                    String uuid) {
    this.id = id;
    this.sis_teacher_id = sis_teacher_id;
    this.default_view = default_view;
    this.migration_id = migration_id;
    this.wiki_id = wiki_id;
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return "CourseData " +
      "id=" + id +
      ", sis_teacher_id='" + sis_teacher_id + '\'' +
      ", default_view='" + default_view + '\'' +
      ", migration_id=" + migration_id +
      ", wiki_id=" + wiki_id
      + ", uuid=" + uuid + "\n";
  }
}

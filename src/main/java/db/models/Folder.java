package db.models;


import org.springframework.lang.Nullable;

public class Folder {

  @Nullable
  public long id;

  public String name;

  public String full_name;

  public long context_id;

  public String context_type;

  @Nullable
  public long parent_folder_id;

  public String workflow_state;

  @Nullable
  public int position;

  @Nullable
  public long migration_id;

  public Folder(long id, String name, String full_name, long context_id, String context_type, long parent_folder_id,
                String workflow_state, int position, long migration_id) {
    super();
    this.id = id;
    this.name = name;
    this.full_name = full_name;
    this.context_id = context_id;
    this.context_type = context_type;
    this.parent_folder_id = parent_folder_id;
    this.workflow_state = workflow_state;
    this.position = position;
  }

  @Override
  public String toString() {
    return "Folder " +
      "id=" + id +
      ", name='" + name + '\'' +
      ", full_name='" + full_name + '\'' +
      ", context_id=" + context_id +
      ", context_type='" + context_type + '\'' +
      ", parent_folder_id=" + parent_folder_id +
      ", workflow_state='" + workflow_state + '\'' +
      ", position=" + position +
      ", migration_id=" + migration_id;
  }
}

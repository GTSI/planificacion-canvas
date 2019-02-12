package db.models;

import org.springframework.lang.Nullable;

public class User {

  @Nullable
  public long id;

  public String name;

  public String sortable_name;

  @Nullable
  public String short_name;

  public String uuid;

  public String workflow_state;

  @Nullable
  public long migration_id;

  public User(long id,
              String name,
              String sortable_name,
              String short_name,
              String uuid,
              String workflow_state,
              long migration_id) {
    this.id = id;
    this.name = name;
    this.sortable_name = sortable_name;
    this.short_name = short_name;
    this.uuid = uuid;
    this.workflow_state = workflow_state;
    this.migration_id = migration_id;
  }

  @Override
  public String toString() {
    return "User" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", sortable_name='" + sortable_name + '\'' +
      ", short_name='" + short_name + '\'' +
      ", uuid='" + uuid + '\'' +
      ", workflow_state='" + workflow_state + '\'' +
      ", migration_id=" + migration_id;
  }

}

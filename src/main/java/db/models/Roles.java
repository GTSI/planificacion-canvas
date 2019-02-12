package db.models;


public class Roles {

  private long id;

  private String workflow_state;

  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getWorkflow_state() {
    return workflow_state;
  }

  public void setWorkflow_state(String workflow_state) {
    this.workflow_state = workflow_state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Roles(long id, String workflow_state, String name) {
    this.id = id;
    this.workflow_state = workflow_state;
    this.name = name;
  }
}

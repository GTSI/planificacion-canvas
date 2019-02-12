package db.models;


import org.springframework.lang.Nullable;

public class CommunicationChannel {

  @Nullable
  public long id;


  private String path;

  private String path_type;

  private int position;

  private long user_id;

  private long pseudonym_id;

  private String workflow_state;

  public CommunicationChannel(long id, String path, String path_type, int position, long user_id, long pseudonym_id, String workflow_state) {
    this.id = id;
    this.path = path;
    this.path_type = path_type;
    this.position = position;
    this.user_id = user_id;
    this.pseudonym_id = pseudonym_id;
    this.workflow_state = workflow_state;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath_type() {
    return path_type;
  }

  public void setPath_type(String path_type) {
    this.path_type = path_type;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  public long getPseudonym_id() {
    return pseudonym_id;
  }

  public void setPseudonym_id(long pseudonym_id) {
    this.pseudonym_id = pseudonym_id;
  }

  public String getWorkflow_state() {
    return workflow_state;
  }

  public void setWorkflow_state(String workflow_state) {
    this.workflow_state = workflow_state;
  }
}

package db.models;

import org.springframework.lang.Nullable;

public class Wiki {

  @Nullable
  private long id;

  private String title;

  private boolean has_no_front_page;

  @Nullable
  private String front_page_url;

  public Wiki(long id, String title, boolean has_no_front_page, String front_page_url) {
    this.id = id;
    this.title = title;
    this.has_no_front_page = has_no_front_page;
    this.front_page_url = front_page_url;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isHas_no_front_page() {
    return has_no_front_page;
  }

  public void setHas_no_front_page(boolean has_no_front_page) {
    this.has_no_front_page = has_no_front_page;
  }

  public @Nullable String getFront_page_url() {
    return front_page_url;
  }

  public void setFront_page_url(String front_page_url) {
    this.front_page_url = front_page_url;
  }
}

package db.models;

import java.sql.Timestamp;

public class EnrollmentState {
  private long enrollment_id;
  private String state;
  private boolean state_is_current;
  private Timestamp state_started_at;
  private Timestamp state_valid_until;
  private boolean restricted_access;
  private boolean access_is_current;
  private int lock_verion;

  public long getEnrollment_id() {
    return enrollment_id;
  }

  public void setEnrollment_id(long enrollment_id) {
    this.enrollment_id = enrollment_id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public boolean isState_is_current() {
    return state_is_current;
  }

  public void setState_is_current(boolean state_is_current) {
    this.state_is_current = state_is_current;
  }

  public Timestamp getState_started_at() {
    return state_started_at;
  }

  public void setState_started_at(Timestamp state_started_at) {
    this.state_started_at = state_started_at;
  }

  public Timestamp getState_valid_until() {
    return state_valid_until;
  }

  public void setState_valid_until(Timestamp state_valid_until) {
    this.state_valid_until = state_valid_until;
  }

  public boolean isRestricted_access() {
    return restricted_access;
  }

  public void setRestricted_access(boolean restricted_access) {
    this.restricted_access = restricted_access;
  }

  public boolean isAccess_is_current() {
    return access_is_current;
  }

  public void setAccess_is_current(boolean access_is_current) {
    this.access_is_current = access_is_current;
  }

  public int getLock_verion() {
    return lock_verion;
  }

  public void setLock_verion(int lock_verion) {
    this.lock_verion = lock_verion;
  }

  public EnrollmentState(long enrollment_id,
                         String state,
                         boolean state_is_current,
                         Timestamp state_started_at,
                         Timestamp state_valid_until,
                         boolean restricted_access,
                         boolean access_is_current,
                         int lock_verion) {
    this.enrollment_id = enrollment_id;
    this.state = state;
    this.state_is_current = state_is_current;
    this.state_started_at = state_started_at;
    this.state_valid_until = state_valid_until;
    this.restricted_access = restricted_access;
    this.access_is_current = access_is_current;
    this.lock_verion = lock_verion;
  }
}

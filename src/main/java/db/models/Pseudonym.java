package db.models;


import org.springframework.lang.Nullable;

import java.util.Objects;

public class Pseudonym {

  @Nullable
  public long id;


  public long user_id;

  public long account_id;

  public String unique_id;

  public String crypted_password;

  @Nullable
  public String password_salt;

  public long login_count;

  public String sis_user_id;

  @Nullable
  public long communication_channel_id;

  public Pseudonym(long id,
                   long user_id,
                   long account_id,
                   String unique_id,
                   String crypted_password,
                   String password_salt,
                   long login_count, String sis_user_id, long communication_channel_id) {
    this.id = id;
    this.user_id = user_id;
    this.account_id = account_id;
    this.unique_id = unique_id;
    this.crypted_password = crypted_password;
    this.password_salt = password_salt;
    this.login_count = login_count;
    this.sis_user_id = sis_user_id;
    this.communication_channel_id = communication_channel_id;
  }

  @Override
  public String toString() {
    return "Pseudonym " +
            "id=" + id +
            ", user_id=" + user_id +
            ", account_id=" + account_id +
            ", unique_id='" + unique_id + '\'' +
            ", crypted_password='" + crypted_password + '\'' +
            ", password_salt='" + password_salt + '\'' +
            ", login_count=" + login_count +
            ", sis_user_id='" + sis_user_id + '\'' +
            ", communication_channel_id=" + communication_channel_id ;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pseudonym pseudonym = (Pseudonym) o;
    return user_id == pseudonym.user_id &&
            Objects.equals(unique_id, pseudonym.unique_id);
  }

  @Override
  public int hashCode() {
    return unique_id.hashCode();
  }
}

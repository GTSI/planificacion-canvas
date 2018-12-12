package db.models;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class Pseudonym {

  @Nullable
  public long id;


  @NotNull
  public long user_id;

  @NotNull
  public long account_id;

  @NotNull
  public String unique_id;

  @NotNull
  public String crypted_password;

  @Nullable
  public String password_salt;

  @NotNull
  public long login_count;

  @NotNull
  public String sis_user_id;

  @Nullable
  public long communication_channel_id;

  public Pseudonym(long id, long user_id, long account_id, String unique_id, String crypted_password, String password_salt, long login_count, String sis_user_id, long communication_channel_id) {
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
}

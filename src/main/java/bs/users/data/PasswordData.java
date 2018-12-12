package bs.users.data;

public final class PasswordData {
  public final String password;
  public final String crypted_password;
  public final String salt_password;

  public PasswordData(String password, String crypted_password, String salt_password) {
    this.password = password;
    this.crypted_password = crypted_password;
    this.salt_password = salt_password;
  }
}

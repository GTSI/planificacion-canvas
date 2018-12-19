package helpers;

import db.models.Pseudonym;

import java.util.List;

public class PseudonymsHelper {
  public static boolean hasPseudonymEmailsAsUniqueId(List<Pseudonym> pseudonyms) {
    for(Pseudonym pseudonym: pseudonyms) {
      if(EmailHelper.isValidEmailAddress(pseudonym.unique_id)) return true;
    }
    return false;
  }

  public static boolean hasUsernameAsUniqueId(List<Pseudonym> pseudonyms, String unique_id) {
    for(Pseudonym pseudonym: pseudonyms) {
      if(pseudonym.unique_id.equals(unique_id)) return true;
    }

    return false;
  }
}

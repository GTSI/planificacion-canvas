package db.daos;

import db.models.Wiki;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WikisDao extends AbstractDao implements Dao<Wiki>{
  public WikisDao(Connection conn) {
    super(conn);
  }

  @Override
  public Optional<Wiki> get(long id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Wiki> getAll() throws SQLException {
    return null;
  }

  @Override
  public long save(Wiki wiki) throws SQLException {
    return 0;
  }

  @Override
  public Wiki saveAndRetrieveIntance(Wiki wiki) throws SQLException {
    PreparedStatement psfCrearWiki = null;
    try {

      assert this.getConn() != null;
      psfCrearWiki = this.getConn().prepareStatement("INSERT INTO wikis(title, created_at, "
        + "updated_at, has_no_front_page, front_page_url)"
        + "	VALUES (?, NOW(), NOW(), ?, ?);", Statement.RETURN_GENERATED_KEYS);

      assert psfCrearWiki != null;
      psfCrearWiki.setString(1, Objects.requireNonNull(wiki.getTitle())); // title
      psfCrearWiki.setBoolean(2, wiki.isHas_no_front_page()); // has_no_front_page
      psfCrearWiki.setString(3, Objects.requireNonNull(wiki.getFront_page_url())); // front_page_url

      psfCrearWiki.executeUpdate();

      ResultSet rsCrearWiki = psfCrearWiki.getGeneratedKeys();

      assert rsCrearWiki != null;
      rsCrearWiki.next();

      long idWiki = rsCrearWiki.getLong(1);

      DbUtils.close(psfCrearWiki);
      DbUtils.close(rsCrearWiki);

      wiki.setId(idWiki);

      return wiki;

    } catch (SQLException e) {
      e.printStackTrace();

      return null;
    }
  }

  @Override
  public List<Long> saveAll(List<Wiki> t) {
    return null;
  }

  @Override
  public void update(Wiki wiki, String[] params) {

  }

  @Override
  public void delete(Wiki wiki) {

  }

  @Override
  public int count() throws SQLException {
    return 0;
  }
}

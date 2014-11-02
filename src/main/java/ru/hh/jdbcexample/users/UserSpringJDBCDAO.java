package ru.hh.jdbcexample.users;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Set;

public class UserSpringJDBCDAO implements UserDAO {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  public UserSpringJDBCDAO(final DataSource dataSource) {

    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("users")
            .usingGeneratedKeyColumns("user_id");
  }

  @Override
  public void insert(final User user) {

    if (user.getId() != null) {
      throw new IllegalArgumentException("can not insert " + user + " with already assigned id");
    }

    final ImmutableMap<String, Object> params = ImmutableMap.of(
            "first_name", user.getFirstName(),
            "last_name", user.getLastName()
    );

    final int userId = simpleJdbcInsert.executeAndReturnKey(params).intValue();

    user.setId(userId);
  }

  @Override
  public Optional<User> get(final int userId) {

    final String query = "SELECT user_id, first_name, last_name FROM users WHERE user_id = :user_id";

    final ImmutableMap<String, Object> params = ImmutableMap.of("user_id", userId);

    final User user;
    try {
      user = namedParameterJdbcTemplate.queryForObject(query, params, rowToUser);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
    return Optional.of(user);
  }

  @Override
  public Set<User> getAll() {

    final String query = "SELECT user_id, first_name, last_name FROM users";

    return ImmutableSet.copyOf(jdbcTemplate.query(query, rowToUser));
  }

  @Override
  public void update(final User user) {

    if (user.getId() == null) {
      throw new IllegalArgumentException("can not update " + user + " without id");
    }

    final String query = "UPDATE users SET first_name = :first_name, last_name = :last_name WHERE user_id = :user_id";

    final ImmutableMap<String, Object> params = ImmutableMap.of(
            "first_name", user.getFirstName(),
            "last_name", user.getLastName(),
            "user_id", user.getId()
    );

    namedParameterJdbcTemplate.update(query, params);
  }

  @Override
  public void delete(final int userId) {

    final String query = "DELETE FROM users WHERE user_id = :user_id";

    final ImmutableMap<String, Object> params = ImmutableMap.of("user_id", userId);

    namedParameterJdbcTemplate.update(query, params);
  }

  private static final RowMapper<User> rowToUser = (resultSet, rowNum) ->
          User.existing(
                  resultSet.getInt("user_id"),
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name")
          );
}

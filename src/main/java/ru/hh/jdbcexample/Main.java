package ru.hh.jdbcexample;

import org.postgresql.ds.PGSimpleDataSource;
import ru.hh.jdbcexample.settings.Settings;
import ru.hh.jdbcexample.users.User;
import ru.hh.jdbcexample.users.UserDAO;
import ru.hh.jdbcexample.users.UserPureJDBCDAO;
import ru.hh.jdbcexample.users.UserSpringJDBCDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static ru.hh.jdbcexample.utils.DataSourceUtils.pgSimpleDataSource;
import static ru.hh.jdbcexample.settings.SettingsUtils.loadSettings;

class Main {

  public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException {

    final Settings settings = loadSettings();

    final PGSimpleDataSource dataSource = pgSimpleDataSource(
            settings.database.url, settings.database.user, settings.database.password
    );

    System.out.println("Pure JDBC example");
    play(new UserPureJDBCDAO(dataSource));

    System.out.println("Spring JDBC example");
    play(new UserSpringJDBCDAO(dataSource));
  }

  private static void play(final UserDAO userDAO) {

    final User user = User.create("Gordon", "Freeman");
    userDAO.insert(user);
    System.out.println("persisted " + user);
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    user.setFirstName("Morgan");
    userDAO.update(user);
    System.out.println("updated Gordon to Morgan");
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    userDAO.delete(user.getId());
    System.out.println("deleted user with id " + user.getId());
    System.out.println("users in db: " + userDAO.getAll());
    System.out.println();

    final Optional<User> emptyMorganFreeman = userDAO.get(user.getId());
    System.out.println("tried to get user by " + user.getId() + " but got " + emptyMorganFreeman);
    System.out.println();
  }

  private Main() {
  }
}

package ru.hh.jdbcexample.settings;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabaseSettings {

  public final String url;
  public final String user;
  public final String password;

  DatabaseSettings(final String url, final String user, final String password) {
    this.url = checkNotNull(url);
    this.user = checkNotNull(user);
    this.password = checkNotNull(password);
  }
}

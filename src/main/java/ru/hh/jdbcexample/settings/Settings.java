package ru.hh.jdbcexample.settings;

import static com.google.common.base.Preconditions.checkNotNull;

public class Settings {

  public final DatabaseSettings database;

  Settings(final DatabaseSettings database) {
    this.database = checkNotNull(database);
  }
}
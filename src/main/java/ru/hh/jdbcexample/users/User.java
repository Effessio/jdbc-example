package ru.hh.jdbcexample.users;

public class User {

  private Integer id;  // problem: id is null for new user and not null for existing user
                       // seems that NewUser and PersistedUser are distinct classes
  private String firstName;
  private String lastName;

  // public factory method to create new user from outside
  public static User create(final String firstName, final String lastName) {
    return new User(null, firstName, lastName);
  }

  // package factory method to load user from db
  // id parameter is int - not Integer - existing user should always have id
  static User existing(final int id, final String firstName, final String lastName) {
    return new User(id, firstName, lastName);
  }

  // private constructor, only factory methods can be used
  private User(final Integer id, final String firstName, final String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Integer getId() {
    return id;
  }

  // setter is package - not public - to prevent changing id from outside
  // also id parameter is int, not Integer to prevent setting null
  void setId(final int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    final User thatUser = (User) that;
    if (firstName != null ? !firstName.equals(thatUser.firstName) : thatUser.firstName != null) return false;
    if (id != null ? !id.equals(thatUser.id) : thatUser.id != null) return false;
    //noinspection RedundantIfStatement
    if (lastName != null ? !lastName.equals(thatUser.lastName) : thatUser.lastName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format(
            "%s{id=%d, firstName='%s', lastName='%s'}",
            getClass().getSimpleName(), id, firstName, lastName
    );
  }
}

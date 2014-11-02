package ru.hh.jdbcexample.users;

// package instead of public visibility
abstract class AbstractUser {  // warn: inheritance of persistent entities is not common practice

  // fuck getters and setters: we are immutable
  // also can not shoot in leg if change some field while already in Set
  public final String firstName;
  public final String lastName;

  protected AbstractUser(final String firstName, final String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public boolean equals(final Object that) {
    if (this == that) return true;
    if (that == null || getClass() != that.getClass()) return false;

    final AbstractUser thatUser = (AbstractUser) that;

    if (firstName != null ? !firstName.equals(thatUser.firstName) : thatUser.firstName != null) return false;
    //noinspection RedundantIfStatement
    if (lastName != null ? !lastName.equals(thatUser.lastName) : thatUser.lastName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = firstName != null ? firstName.hashCode() : 0;
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    return result;
  }

  // helps to implement toString in children
  protected String fieldsToString() {
    return String.format("firstName='%s', lastName='%s'", firstName, lastName);
  }
}

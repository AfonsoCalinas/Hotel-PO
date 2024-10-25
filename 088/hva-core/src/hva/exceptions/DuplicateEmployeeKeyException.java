package hva.exceptions;

public class DuplicateEmployeeKeyException extends Exception {

  private final String key;

  public DuplicateEmployeeKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
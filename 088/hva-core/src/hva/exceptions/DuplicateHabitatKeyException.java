package hva.exceptions;

public class DuplicateHabitatKeyException extends Exception {

  private final String key;

  public DuplicateHabitatKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
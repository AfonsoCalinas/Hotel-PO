package hva.exceptions;

public class DuplicateTreeKeyException extends Exception {

  private final String key;

  public DuplicateTreeKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
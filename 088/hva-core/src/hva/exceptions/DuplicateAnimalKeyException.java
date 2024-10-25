package hva.exceptions;

public class DuplicateAnimalKeyException extends Exception {

  private final String key;

  public DuplicateAnimalKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
package hva.exceptions;

public class DuplicateSpeciesKeyException extends Exception {

  private final String key;

  public DuplicateSpeciesKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
package hva.exceptions;

public class DuplicateVaccineKeyException extends Exception {

  private final String key;

  public DuplicateVaccineKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
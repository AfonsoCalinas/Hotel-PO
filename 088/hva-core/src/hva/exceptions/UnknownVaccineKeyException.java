package hva.exceptions;

public class UnknownVaccineKeyException extends Exception {

    private final String key;

    public UnknownVaccineKeyException(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

}

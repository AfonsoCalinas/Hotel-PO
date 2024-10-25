package hva.exceptions;

public class UnknownHabitatKeyException extends Exception {

    private final String key;

    public UnknownHabitatKeyException(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

}

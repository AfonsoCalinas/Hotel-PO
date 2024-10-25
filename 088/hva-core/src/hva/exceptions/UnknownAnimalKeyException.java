package hva.exceptions;

public class UnknownAnimalKeyException extends Exception {

    private final String key;

    public UnknownAnimalKeyException(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

}
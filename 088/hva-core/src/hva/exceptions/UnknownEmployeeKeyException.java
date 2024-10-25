package hva.exceptions;

public class UnknownEmployeeKeyException extends Exception {

    private final String key;

    public UnknownEmployeeKeyException(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

}

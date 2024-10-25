package hva.exceptions;

public class UnknownSpeciesKeyException extends Exception {

    private final String key;

    public UnknownSpeciesKeyException(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

}

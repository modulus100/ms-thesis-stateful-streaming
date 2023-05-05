package ms.thesis.log.state.consumer;

/* Keeps the state of a stateful consumer. Could be replaced by NobeX in future. */
public class State {
  private byte[] data;

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}

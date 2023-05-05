package ms.thesis.log.state.consumer;

import ms.thesis.common.record.ByteArrayRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

import static java.util.Objects.requireNonNull;


public class CountingConsumer implements StatefulConsumer {

  private static final long serialVersionUID = 0L;

  private static final VarHandle LONG_VAR_HANDLE =
      MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);

  private static final Logger LOGGER = LoggerFactory.getLogger(CountingConsumer.class);

  private final String name;

  public CountingConsumer(String name) {
    this.name = requireNonNull(name);
  }

  @Override
  public State accept(ByteArrayRecord record, State state) {
    if (state == null) {
      state = new State();
    }

    byte[] data = state.getData();
    if (data == null) {
      data = new byte[Long.BYTES];
      state.setData(data);
    }

    long c = (long) LONG_VAR_HANDLE.get(data, 0);
    c += 1;
    LONG_VAR_HANDLE.set(data, 0, c);

    LOGGER.debug("{}: count = {}", name, c);

    return state;
  }
}

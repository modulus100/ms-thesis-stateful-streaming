package ms.thesis.log.state.consumer;


import ms.thesis.common.record.ByteArrayRecord;

import java.io.Serializable;

/**
 * Implementations must be stateless. The state must be kept in the {@link State} object.
 */
@FunctionalInterface
public interface StatefulConsumer extends Serializable {

  /**
   * @param record a new data record
   * @param state  the current state
   * @return the updated state
   */
  State accept(ByteArrayRecord record, State state); // TODO Record and State should be NobeX objects in future
}

package ms.thesis.log.state.record;

import ms.thesis.common.record.ByteArrayRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class DynatraceRecordSerde implements Serde<ByteArrayRecord> {

  @Override
  public Serializer<ByteArrayRecord> serializer() {
    return new DynatraceRecordSerializer();
  }

  @Override
  public Deserializer<ByteArrayRecord> deserializer() {
    return new DynatraceRecordDeserializer();
  }

  public static class DynatraceRecordSerializer implements Serializer<ByteArrayRecord> {

    @Override
    public byte[] serialize(String topic, ByteArrayRecord record) {
      return record.data();
    }

  }

  public static class DynatraceRecordDeserializer implements Deserializer<ByteArrayRecord> {

    @Override
    public ByteArrayRecord deserialize(String topic, byte[] data) {
      return new ByteArrayRecord(data);
    }

  }
}

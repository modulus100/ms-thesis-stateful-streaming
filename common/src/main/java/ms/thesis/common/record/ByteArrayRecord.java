package ms.thesis.common.record;


public class ByteArrayRecord {
    private final byte[] data;

    public ByteArrayRecord(byte[] data) {
        this.data = data;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public String toString() {
        return "Record{" + "data=" + Util.bytesToHex(data, 0, 16) + "...}";
    }

}

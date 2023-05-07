package lsieun.mqtt;

public enum MQTTControlPacketType {
    Reserved00(0),
    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    PUBREC(5),
    PUBREL(6),
    PUBCOMP(7),
    SUBSCRIBE(8),
    SUBACK(9),
    UNSUBSCRIBE(10),
    UNSUBACK(11),
    PINGREQ(12),
    PINGRESP(13),
    DISCONNECT(14),
    Reserved15(15);
    public final int val;

    MQTTControlPacketType(int val) {
        this.val = val;
    }

    public static MQTTControlPacketType parseInt(int val) {
        MQTTControlPacketType[] values = values();
        for (MQTTControlPacketType item : values) {
            if (item.val == val) {
                return item;
            }
        }

        throw new IllegalArgumentException("val = " + val);
    }
}

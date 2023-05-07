package lsieun.mqtt;

import lsieun.utils.ByteDashboard;
import lsieun.utils.HexFormat;
import lsieun.utils.HexUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MQTTControlPacketFormat {
    public static void interpret(byte[] bytes) {
        System.out.println(HexUtils.format(bytes, HexFormat.FORMAT_FF_FF));

        ByteDashboard bd = new ByteDashboard(bytes);
        int firstByte = bd.readUnsignedByte();

        int typeVal = (firstByte >> 4) & 0x0F;

        MQTTControlPacketType packetType = MQTTControlPacketType.parseInt(typeVal);
        System.out.println(packetType);

        if (packetType == MQTTControlPacketType.PUBLISH) {
            throw new RuntimeException("flags is not processed: " + packetType);
        }

        int remainingLength = readRemainingLength(bd);
        System.out.println("Remaining Length = " + remainingLength);

        String protocolName = readUTF8(bd);
        System.out.println(protocolName);

        int protocolLevel = readProtocolLevel(bd);
        System.out.println("Protocol Level = " + protocolLevel);

        List<String> connectFlags = readConnectFlags(bd);
        System.out.println("Connect Flags = " + connectFlags);

        int keepAlive = readKeepAlive(bd);
        System.out.println("Keep Alive = " + keepAlive);

        String clientIdentifier = readUTF8(bd);
        System.out.println("Client Identifier = " + clientIdentifier);
    }

    private static int readRemainingLength(ByteDashboard bd) {
        int multiplier = 1;
        int value = 0;

        boolean hasNext;
        do {
            int encodedByte = bd.readUnsignedByte();
            value += (encodedByte & 0x7F) * multiplier;
            multiplier *= 128;

            if (multiplier > 128 * 128 * 128) {
                throw new RuntimeException("Malformed Remaining Length");
            }

            hasNext = (encodedByte & 0x80) > 0;
        }
        while (hasNext);

        return value;
    }

    private static String readUTF8(ByteDashboard bd) {
        int length = bd.readUnsignedShort();
        byte[] bytes = bd.nextN(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static int readProtocolLevel(ByteDashboard bd) {
        return bd.readUnsignedByte();
    }

    private static List<String> readConnectFlags(ByteDashboard bd) {
        int val = bd.readUnsignedByte();

        List<String> list = new ArrayList<>();
        if ((val & 0b1000_0000) != 0) {
            list.add("User Name Flag");
        }
        if ((val & 0b0100_0000) != 0) {
            list.add("Password Flag");
        }
        if ((val & 0b0010_0000) != 0) {
            list.add("Will Retain");
        }
        if ((val & 0b0001_1000) != 0) {
            list.add("Will QoS");
        }
        if ((val & 0b0000_0100) != 0) {
            list.add("Will Flag");
        }
        if ((val & 0b0000_0010) != 0) {
            list.add("Clean Session");
        }
        if ((val & 0b0000_0001) != 0) {
            list.add("Reserved");
        }
        return list;
    }

    private static int readKeepAlive(ByteDashboard bd) {
        return bd.readUnsignedShort();
    }

    public static void main(String[] args) {
        String hexStr1 = "103000044D5154540400003C002436386265316163322D633638312D346261382D383461302D643738616231333161656630";
        String hexStr2 = "103200064D51497364700300003C002436386265316163322D633638312D346261382D383461302D643738616231333161656630";

        String hexStr3 = "103000044D5154540402003C002436613663636363342D383839322D343932622D383866652D336139306466623163653866";
        String hexStr4 = "103200064D51497364700302003C002436613663636363342D383839322D343932622D383866652D336139306466623163653866";

        byte[] bytes = HexUtils.parse(hexStr2, HexFormat.FORMAT_FF_FF);
        System.out.println(HexUtils.format(bytes, HexFormat.FORMAT_FF_SPACE_FF_16));
        interpret(bytes);
    }
}

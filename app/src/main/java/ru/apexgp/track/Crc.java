package ru.apexgp.track;


public class Crc {
    public static int crc16(byte[] data, int len) {
        int crc = 0;

        for (int i = 0; i < len; i++) {
            crc ^= data[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = 0x8408 ^ crc >>> 1;
                } else {
                    crc >>>= 1;
                }
            }
        }
        return crc;
    }

    public static byte crc8(byte[] buffer, int len) {
        byte crc = (byte) 0xFF;

        for (int j = 0; j < len; ++j) {
            crc ^= buffer[j];

            for (int i = 0; i < 8; i++) {
                crc = (crc & 0x80) != 0 ? (byte) ((crc << 1) ^ 0x31) : (byte) (crc << 1);
            }
        }

        return crc;
    }
}

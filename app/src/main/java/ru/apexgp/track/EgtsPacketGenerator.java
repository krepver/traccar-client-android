package ru.apexgp.track;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.CRC32;

public class EgtsPacketGenerator {

    private static byte[] authTemplate = new byte[]{
            0x01, // 0 protocol version
            0x00, // 1 security key id
            0x00, // 2 flags
            0x0b, // 3 header length (11)
            0x00, // 4 header encoding
            0x22, 0x00, // 5,6 frame data length
            0x00, 0x00, // 7,8 packet id
            0x01, // 9 packet type (1 - app data)
            0x00, // 10 header checksum
                0x17, 0x00, // 11,12 record length
                0x00, 0x00, // 13,14 record number
                0x01, // 15 record flags
                0x00, 0x00, 0x00, 0x00, // 16,17,18,19 oid
                0x01, // 20 source service type (1 - AUTH SERVICE)
                0x01, // 21 recipient service type
                    0x01, // 22 subrecord type (TERM_IDENTITY)
                    0x14, 0x00, // 23 subrecord length (22)
                        0x00, 0x00, 0x00, 0x00, // 24,25,26,27 term id (oid)
                        0x02, // 28 flags (00000010 - imei)
                        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, // 29,30,31,32,33,34,35,36,37,38,39,40,41,42,43 imei
            0x00, 0x00 // 44,45 frame data checksum
    };

    public byte[] getAuthRequest(int pid, long oid, String imei) {
        byte[] request = new byte[authTemplate.length];
        System.arraycopy(authTemplate,0, request,0, authTemplate.length);
        byte pidPos1 = 7;
        byte pidPos2 = 8;
        byte oid1Pos1 = 16;
        byte oid1Pos2 = 17;
        byte oid1Pos3 = 18;
        byte oid1Pos4 = 19;
        byte oid2Pos1 = 24;
        byte oid2Pos2 = 25;
        byte oid2Pos3 = 26;
        byte oid2Pos4 = 27;
        byte imeiPos = 29;
        byte headerCrcPos = 10;
        byte frameCrc1 = 44;
        byte frameCrc2 = 45;

        request[pidPos1] = (byte)(pid & 0xff);
        request[pidPos2] = (byte)((pid & 0xff00) >> 8);
        request[oid1Pos1] = (byte)(oid & 0xff);
        request[oid1Pos2] = (byte)((oid & 0xff00) >> 8);
        request[oid1Pos3] = (byte)((oid & 0xff0000) >> 16);
        request[oid1Pos4] = (byte)((oid & 0xff000000) >> 24);
        request[oid2Pos1] = (byte)(oid & 0xff);
        request[oid2Pos2] = (byte)((oid & 0xff00) >> 8);
        request[oid2Pos3] = (byte)((oid & 0xff0000) >> 16);
        request[oid2Pos4] = (byte)((oid & 0xff000000) >> 24);

        System.arraycopy(imei.toCharArray(), 0, request, imeiPos, 15);

        request[headerCrcPos] = Crc.crc8(request, request[3]);

        int headerLength = authTemplate[3];
        int frameLength = authTemplate[5] | (authTemplate[6] << 8);
        int crc = Crc.crc16(Arrays.copyOfRange(request,headerLength, frameLength+headerLength), frameLength);
        request[frameCrc1] = (byte)(crc & 0xff);
        request[frameCrc2] = (byte)((crc & 0xff00) >> 8);

        return request;
    }
}

/*
* 861693033000928
* */

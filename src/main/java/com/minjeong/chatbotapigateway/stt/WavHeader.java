package com.minjeong.chatbotapigateway.stt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WavHeader {

    public static byte[] wavHeader() {
        return ByteBuffer.allocate(44)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put("RIFF".getBytes())
                .putInt(0)
                .put("WAVE".getBytes())
                .put("fmt ".getBytes())
                .putInt(16)
                .putShort((short) 1)
                .putShort((short) 1)
                .putInt(8000)
                .putInt(16000)
                .putShort((short) 2)
                .putShort((short) 16)
                .put("data".getBytes())
                .putInt(0)
                .array();
    }

    public static byte[] wavHeader(byte[] pcmData, int channels) {
        return ByteBuffer.allocate(44 + pcmData.length)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put("RIFF".getBytes())
                .putInt(36 + pcmData.length)
                .put("WAVE".getBytes())
                .put("fmt ".getBytes())
                .putInt(16)
                .putShort((short) 1)
                .putShort((short) channels)
                .putInt(8000)
                .putInt(8000 * channels * 2)
                .putShort((short) (channels * 2))
                .putShort((short) 16)
                .put("data".getBytes())
                .putInt(pcmData.length)
                .put(pcmData)
                .array();
    }
}

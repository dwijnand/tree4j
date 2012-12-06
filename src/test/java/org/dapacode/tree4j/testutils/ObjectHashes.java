package org.dapacode.tree4j.testutils;

import com.thoughtworks.xstream.XStream;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public final class ObjectHashes {
  private static final int BUF_SIZE = 0x1000;

  private ObjectHashes() { /* Utility Class */ }

  public static long getCRCChecksum(final Object o) {
    InputStream in = null;
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new XStream().toXML(o, baos);
      in = new ByteArrayInputStream(baos.toByteArray());

      final CRC32 crc32 = new CRC32();
      final byte[] buf = new byte[BUF_SIZE];

      for (int amt = in.read(buf); amt != -1; amt = in.read(buf)) {
        crc32.update(buf, 0, amt);
      }

      return crc32.getValue();
    } catch (final IOException e) {
      throw new RuntimeException("Failed to calculate the checksum", e);
    } finally {
      close(in);
    }
  }

  private static void close(@Nullable final InputStream in) {
    if (in != null) {
      try {
        in.close();
      } catch (final IOException e) {
        throw new RuntimeException("IOException thrown while closing InputStream", e);
      }
    }
  }
}

package dataset;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class LibraryUsageDumper {

  /**
   * Dumps up to `limit` entries from LIBS_BY_USAGE into `outputPath`, one per line
   * using LibraryUsage.toString() as the representation.
   */
  public static void dumpTopUsage(Iterator<? extends Object> objects, Path outputPath, int limit) {
    try (BufferedWriter writer = Files.newBufferedWriter(
      outputPath,
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING,
      StandardOpenOption.WRITE)) {

      int count = 0;
      while (objects.hasNext() && count < limit) {
        writer.write(objects.next().toString());
        writer.newLine();
        count++;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}


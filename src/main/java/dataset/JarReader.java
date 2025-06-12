package dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipInputStream;

public class JarReader {

  public static void populateLibraryMetadata(Main.LocalArtifact artifact, LibraryMetadata libraryMetadata) {
    try (var in = Files.newInputStream(artifact.localPath());
         var zip = new ZipInputStream(in)) {
      for (var entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        if (!entry.isDirectory()) {
          String path = entry.getName();
          if (path.contains("META-INF/LICENSE")) {
            libraryMetadata.setLicense(new String(zip.readAllBytes()));
            zip.closeEntry();
          }
          if (path.contains("META-INF/MANIFEST")) {
            String content = new String(zip.readAllBytes());
            String[] lines = content.split("\n");
            for (String line : lines) {
              if (libraryMetadata.getLicense() == null && line.toLowerCase().contains("license")) {
                libraryMetadata.setLicense(line);
              }
              if (line.toLowerCase().contains("github.com/")) {
                libraryMetadata.setRepo(line);
              }
            }
          }
        }
        zip.closeEntry();
      }
    } catch (IOException e) {
//      System.err.println("Error reading " + artifact.localPath());
    }
  }

}

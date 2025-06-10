package dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;

public class JarLicense {

  public String licenseFileContent;

  public JarLicense(ProjectDatasetExtractor.LocalArtifact artifact) {
    try (var in = Files.newInputStream(artifact.localPath());
         var zip = new ZipInputStream(in)) {
      for (var entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        if (licenseFileContent != null) {
          break;
        }
        if (!entry.isDirectory()) {
          String path = entry.getName();
          if (path.contains("LICENSE")) {
            licenseFileContent = new String(zip.readAllBytes());
            zip.closeEntry();
          } else if (path.contains("MANIFEST")) {
            String content = new String(zip.readAllBytes());
            String[] lines = content.split("\n");
            for (String line : lines) {
              if (line.toLowerCase().contains("license")) {
                licenseFileContent = line;
              }
            }
          }
        }
        zip.closeEntry();
      }
    } catch (IOException e) {
      System.err.println("Error reading " + artifact.localPath());
    }
  }

}

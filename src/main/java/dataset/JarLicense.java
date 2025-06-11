package dataset;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class JarLicense {

  private static final HttpClient CLIENT = HttpClient.newHttpClient();

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
          if (path.contains("META-INF/LICENSE")) {
            licenseFileContent = new String(zip.readAllBytes());
            zip.closeEntry();
          } else if (path.contains("MANIFEST")) {
            String content = new String(zip.readAllBytes());
            String[] lines = content.split("\n");
            for (String line : lines) {
              if (line.toLowerCase().contains("license")) {
                licenseFileContent = line;
                break;
              }
            }
          }
        }
        zip.closeEntry();
      }
    } catch (IOException e) {
//      System.err.println("Error reading " + artifact.localPath());
    }
    if (licenseFileContent == null) {
      String pomUrl = buildPomUrl(artifact.groupId(), artifact.artifactId(), artifact.version());
      try {
        Document pom = downloadPom(pomUrl);
        licenseFileContent = extractLicenseName(pom);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    }
  }

  public static String extractLicenseName(Document doc) throws Exception {
    XPath xp = XPathFactory.newInstance().newXPath();
    // Match: /project/licenses/license/name but using local-name() to ignore xmlns
    String expr = "/*[local-name()='project']"
      + "/*[local-name()='licenses']"
      + "/*[local-name()='license']"
      + "/*[local-name()='url']/text()";
    NodeList nodes = (NodeList) xp.evaluate(expr, doc, XPathConstants.NODESET);

    if (nodes.getLength() > 0) {
      return "License from pom = " + nodes.item(0).getNodeValue().trim();
    }
    return null;
  }

  private static Document downloadPom(String pomUrl) throws Exception {
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(pomUrl))
      .GET()
      .build();
    HttpResponse<InputStream> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofInputStream());
    if (resp.statusCode() != 200) {
      throw new RuntimeException("Failed to fetch POM: HTTP " + resp.statusCode());
    }
    // parse
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    return dbf.newDocumentBuilder().parse(resp.body());
  }

  private static String buildPomUrl(String groupId, String artifactId, String version) {
    String path = groupId.replace('.', '/');
    return String.format(
      "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.pom",
      path, artifactId, version, artifactId, version
    );
  }

  public static String buildPomUrl(ProjectDatasetExtractor.LocalArtifact artifact) {
    return buildPomUrl(artifact.groupId(), artifact.artifactId(), artifact.version());
  }


}

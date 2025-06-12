package dataset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PomReader {

  private static final HttpClient CLIENT = HttpClient.newHttpClient();

  public static void populateLibraryMetadata(Main.LocalArtifact artifact, LibraryMetadata libraryMetadata) {
    String pomUrl = buildPomUrl(artifact);
    try {
      Document pom = downloadPom(pomUrl);
      if (libraryMetadata.getLicense() == null) {
        libraryMetadata.setLicense(extractLicenseName(pom));
      }
      if (libraryMetadata.getRepo() == null) {
        libraryMetadata.setRepo(extractRepoUrl(pom));
      }
    } catch (Exception e) {
      System.err.println("populateLibraryMetadata failed for " + artifact);
    }
  }

  private static String extractRepoUrl(Document doc) throws XPathExpressionException {
    XPath xp = XPathFactory.newInstance().newXPath();
    // Match: /project/repositories/repository/url but using local-name() to ignore xmlns
    String expr = "/*[local-name()='project']"
      + "/*[local-name()='repositories']"
      + "/*[local-name()='repository']"
      + "/*[local-name()='url']/text()";
    NodeList nodes = (NodeList) xp.evaluate(expr, doc, XPathConstants.NODESET);

    if (nodes.getLength() > 0) {
      return nodes.item(0).getNodeValue().trim();
    }
    //if not found lets also look under <scm><url>
    expr = "/*[local-name()='project']"
      + "/*[local-name()='scm']"
      + "/*[local-name()='url']/text()";
    nodes = (NodeList) xp.evaluate(expr, doc, XPathConstants.NODESET);
    if (nodes.getLength() > 0) {
      return nodes.item(0).getNodeValue().trim();
    }


    return null;
  }

  private static String extractLicenseName(Document doc) throws XPathExpressionException {
    NodeList firstChildren = doc.getChildNodes();
    for (int i = 0; i < firstChildren.getLength(); i++) {
      var node = firstChildren.item(i);
      if (node.getNodeType() == Node.COMMENT_NODE && node.getTextContent().toLowerCase().contains("license")) {
        return node.getTextContent();
      }

    }

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

  private static Document downloadPom(String pomUrl) throws IOException, InterruptedException {
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(pomUrl))
      .GET()
      .build();
    HttpResponse<InputStream> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofInputStream());
    if (resp.statusCode() != 200) {
      System.err.println("Failed to fetch POM: HTTP " + resp.statusCode());
    }
    // parse
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    try {
      return dbf.newDocumentBuilder().parse(resp.body());
    } catch (SAXException e) {
      System.err.println("SAXException " + e.getMessage());
    } catch (ParserConfigurationException e) {
      System.err.println("ParserConfigurationException " + e.getMessage());
    }
    return null;
  }

  private static String buildPomUrl(String groupId, String artifactId, String version) {
    String path = groupId.replace('.', '/');
    return String.format(
      "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.pom",
      path, artifactId, version, artifactId, version
    );
  }

  private static String buildPomUrl(Main.LocalArtifact artifact) {
    return buildPomUrl(artifact.groupId(), artifact.artifactId(), artifact.version());
  }

}

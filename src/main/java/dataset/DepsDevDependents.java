package dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class DepsDevDependents {

  private static final String BASE_URL = "https://api.deps.dev/v3alpha/systems/MAVEN/packages";
  private static final HttpClient CLIENT = HttpClient.newHttpClient();
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static class DependentsInfo {
    @JsonProperty("dependentCount")
    public int dependentCount;

    @JsonProperty("directDependentCount")
    public int directDependentCount;

    @JsonProperty("indirectDependentCount")
    public int indirectDependentCount;

    @Override
    public String toString() {
      return String.format(
        "Total dependents: %d%nDirect dependents: %d%nIndirect dependents: %d",
        dependentCount, directDependentCount, indirectDependentCount
      );
    }
  }


  public static DependentsInfo fetchDependents(Main.LocalArtifact artifact) {
    try {
      return fetchDependents(artifact.groupId(), artifact.artifactId(), artifact.version());
    } catch (Exception e) {
      return new DependentsInfo();
    }
  }

  public static DependentsInfo fetchDependents(String groupId, String artifactId, String version) throws IOException, InterruptedException {
    // URL‐encode "groupId:artifactId"
    String pkg = URLEncoder.encode(groupId + ":" + artifactId, StandardCharsets.UTF_8);
    String ver = URLEncoder.encode(version, StandardCharsets.UTF_8);

    String url = String.format("%s/%s/versions/%s:dependents", BASE_URL, pkg, ver);

    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Accept", "application/json")
      .GET()
      .build();

    HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
    if (resp.statusCode() != 200) {
      throw new IOException("Unexpected HTTP response: " + resp.statusCode() + " – " + resp.body());
    }

    return MAPPER.readValue(resp.body(), DependentsInfo.class);
  }

  public static void main(String[] args) {
    try {
      System.out.println(fetchDependents("displaytag", "displaytag", "1.2"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}


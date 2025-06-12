package dataset;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GHApi {


  private static final GitHub github;

  static {
    String githubToken = System.getenv("GITHUB_TOKEN");
    if (githubToken == null) {
      System.err.println("GITHUB_TOKEN environment variable not set.");
    }
    try {
      github = new GitHubBuilder().withOAuthToken(githubToken).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public static void main(String[] args) {
    getLicenseForRepo("square/retrofit");
  }

  public static String getLicenseForRepo(String url) {
    try {
      var license = github.getRepository(getOwnerAndRepo(url)).getLicense();
      return license.getKey();
    } catch (IOException e) {
    }
    return null;
  }

  public static int getStarsForRepo(String url) {
    try {
      return github.getRepository(getOwnerAndRepo(url)).getStargazersCount();
    } catch (IOException e) {
    }
    return -1;
  }

  private static String getOwnerAndRepo(String githubUrl) {
    // The regex looks for:
    // ^https?://github\\.com/  - Matches "http://github.com/" or "https://github.com/" at the beginning
    // ([^/]+)                 - Captures the first part after github.com (the owner), stopping at the next '/'
    // /                       - Matches the '/' separator
    // ([^/]+)                 - Captures the second part (the repository name), stopping at the next '/'
    // (?:/.*)?                - Non-capturing group for an optional "/" followed by any characters

    if (githubUrl.endsWith(".git")) {
      githubUrl = githubUrl.substring(0, githubUrl.length() - 4);
    }

    Pattern pattern = Pattern.compile("^https?://github\\.com/([^/]+)/([^/]+)(?:/.*)?$");
    Matcher matcher = pattern.matcher(githubUrl);

    if (matcher.matches()) {
      // Group 1 is the owner, Group 2 is the repository name
      return matcher.group(1) + "/" + matcher.group(2);
    } else {
      return null; // No match found
    }
  }

}

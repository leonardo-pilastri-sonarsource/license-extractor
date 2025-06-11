package dataset;

import java.util.ArrayList;
import java.util.List;

public class Licenses {

  private static final List<String> AGPL = List.of(
    "gnu.org/licenses/agpl");
  private static final List<String> GPL = List.of(
    "gnu.org/licenses/gpl",
    "gnu.org/licenses/old-licenses/gpl",
    "gnu.org/copyleft/gpl");
  private static final List<String> LGPL = List.of(
    "gnu.org/licenses/lgpl",
    "gnu.org/licenses/old-licenses/lgpl",
    "opensource.org/licenses/lgpl",
    "gnu.org/copyleft/lesser.html");

  private static final List<String> FORBIDDEN = new ArrayList<>();

  static {
    FORBIDDEN.addAll(AGPL);
    FORBIDDEN.addAll(GPL);
    // if we want to also detect weak copyleft, we add lgpl
    FORBIDDEN.addAll(LGPL);
  }

  public static boolean isForbidden(String licenseContent) {
    return FORBIDDEN.stream().anyMatch(licenseContent::contains);
  }

}

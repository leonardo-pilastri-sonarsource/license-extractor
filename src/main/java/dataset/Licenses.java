package dataset;

import java.util.List;

public class Licenses {

  private static final List<String> AGPL = List.of("agpl_v3","AGPL 3","GNU Affero General Public License (AGPL) version 3.0","AGPL 3.0");
  private static final List<String> APACHE2 = List.of("apache_v2","Apache 2","Apache 2.0","Apache-2.0","Apache Public License 2.0","The Apache Software License, Version 2.0","Apache License, Version 2.0","Apache License, version 2.0","Apache Software Licenses","Apache License Version 2.0","ASF 2.0","ASL, version 2","The Apache License, Version 2.0");
  private static final List<String> BC = List.of("bouncy_castle","Bouncy Castle Licence");
  private static final List<String> BSD = List.of("bsd","BSD","BSD License","The BSD License","3-Clause BSD License","The BSD 3-Clause License","New BSD License","New BSD license","BSD 3-Clause","BSD-3-Clause","BSD-style license","Trilead Library License (BSD-Like)","Revised BSD","BSD style");
  private static final List<String> CDDL_GPL2 = List.of("cddl_gpl","Dual license: CDDL 1.0 and GPL v2","Dual license consisting of the CDDL v1.0 and GPL v2","CDDL+GPLv2","CDDL+GPL","CDDL+GPL License","Dual license: CDDL 1.1 and GPL v2","Dual license consisting of the CDDL v1.1 and GPL v2","CDDL1_1+GPLv2","Dual License: CDDL 1.0 and GPL V2 with Classpath Exception","CDDL + GPLv2 with classpath exception","CDDL/GPLv2+CE");
  private static final List<String> CDDL = List.of("cddl_v1","CDDL","CDDL 1.0","CDDL 1.1","COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0","Common Development and Distribution License (CDDL) v1.0");
  private static final List<String> EPL1 = List.of("epl_v1","EPL 1.0","Eclipse Public License 1.0","Eclipse Public License - v 1.0","Eclipse Public License, Version 1.0","Eclipse Public License v1.0","EPL");
  private static final List<String> EPL2 = List.of("epl_v2","Eclipse Public License - v 2.0","Eclipse Public License v2.0","Eclipse Public License 2.0");
  private static final List<String> GPL2 = List.of("gpl_v2","GPL 2","GNU General Public License (GPL) version 2.0","GPL 2.0","GNU General Public License (GPL)","GNU General Public Library");
  private static final List<String> GPL_CPE = List.of("gpl_v2_cpe","GPL2 w/ CPE");
  private static final List<String> GPL3 = List.of("gpl_v3","GPL 3","GNU General Public License (GPL) version 3.0","GNU General Public License, Version 3","GPL 3.0");
  private static final List<String> H2 = List.of("h2","The H2 License, Version 1.0");
  private static final List<String> JAXEN = List.of("jaxen","http://jaxen.codehaus.org/license.html","Jaxen");
  private static final List<String> LGPL2 = List.of("lgpl_v2","LGPL 2.0","GNU LGPL 2.0","GNU Lesser General Public License (LGPL), Version 2.0","GNU Lesser General Public License, version 2.0");
  private static final List<String> LGPL2_1 = List.of("lgpl_v2_1","LGPL 2.1","GNU LGPL 2.1","GNU Lesser General Public License (LGPL), Version 2.1","GNU Lesser General Public License, version 2.1","LGPL, version 2.1");
  private static final List<String> LGPL3 = List.of("lgpl_v3","LGPL 3","GNU LGPL 3","GNU LGPL v3","LGPL v.3","GNU LESSER GENERAL PUBLIC LICENSE, Version 3","GNU Lesser General Public Licence","GNU Lesser General Public License","GNU LESSER GENERAL PUBLIC LICENSE");
  private static final List<String> MIT = List.of("mit","MIT","MIT License","MIT license","The MIT License","The MIT license");
  private static final List<String> PUBLIC = List.of("public_domain","Public Domain","Public Domain, per Creative Commons CC0","Common Public License Version 1.0");
  private static final List<String> SONAR = List.of("sonarsource","SonarSource","Commercial");
  private static final List<String> TMATE = List.of("tmate","TMate Open Source License");

  public static final List<List<String>> STRONG_COPY_LEFT = List.of(
    AGPL,
    GPL2,
    GPL3
  );

}

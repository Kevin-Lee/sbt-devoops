logLevel := sbt.Level.Warn

addSbtPlugin("com.geirsson"     % "sbt-ci-release"        % "1.5.7")
addSbtPlugin("org.wartremover"  % "sbt-wartremover"       % "2.3.7")
addSbtPlugin("org.scoverage"    % "sbt-scoverage"         % "1.5.1")
addSbtPlugin("org.scoverage"    % "sbt-coveralls"         % "1.2.7")
addSbtPlugin("org.lyranthe.sbt" % "partial-unification"   % "1.1.2")
addSbtPlugin("io.kevinlee"      % "sbt-docusaur"          % "0.5.0")
addSbtPlugin("io.kevinlee"      % "sbt-devoops-scala"     % "2.6.0")
addSbtPlugin("io.kevinlee"      % "sbt-devoops-sbt-extra" % "2.6.0")
addSbtPlugin("io.kevinlee"      % "sbt-devoops-github"    % "2.6.0")

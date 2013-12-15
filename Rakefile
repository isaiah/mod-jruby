# encoding: utf-8


require 'ant'

directory "pkg/classes"

desc "Clean up build artifacts"
task :clean do
  rm_rf "pkg/classes"
  rm_rf "lib/vertx/ext/*.jar"
end

desc "Compile the extension"
task :compile => "pkg/classes" do |t|
  ant.javac :srcdir => "java", :destdir => t.prerequisites.first,
    :source => "1.7", :target => "1.7", :debug => true,
    :classpath => "${java.class.path}:${sun.boot.class.path}:jars/jruby.jar:jars/vertx-core-2.1M3-SNAPSHOT.jar:jars/vertx-platform-2.1M3-SNAPSHOT.jar:jars/netty-all-4.0.13.Final.jar"
end

desc "Build the jar"
task :jar => [:clean, :compile] do
  ant.jar :basedir => "pkg/classes", :destfile => "lib/vertx/ext/vertx.jar", :includes => "**/*.class"
end

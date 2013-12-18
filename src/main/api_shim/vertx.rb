require 'vertx/ext/vertx.jar'

module Vertx
  def self.logger
    org.vertx.java.platform.impl.JRubyVerticleFactory.container.logger
  end
end

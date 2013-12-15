module Vertx
  module EventBus
    class << self
      def send(address, message, &block)
        self.send_or_pub(true, address, message, block)
      end

      def publish(address, message)
        self.send_or_pub(false, address, message)
      end
    end
  end
end

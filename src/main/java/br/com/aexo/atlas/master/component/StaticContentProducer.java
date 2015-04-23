package br.com.aexo.atlas.master.component;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The static producer.
 */
public class StaticContentProducer extends DefaultProducer {
   private static final Logger LOG = LoggerFactory.getLogger(StaticContentProducer.class);
   private StaticContentEndpoint endpoint;

   public StaticContentProducer(StaticContentEndpoint endpoint) {
      super(endpoint);
      this.endpoint = endpoint;
   }

   public void process(Exchange exchange) throws Exception {
     
   }

   private Message getMessageForResponse(Exchange exchange) {
      if (exchange.getPattern().isOutCapable()) {
         Message out = exchange.getOut();
         out.copyFrom(exchange.getIn());
         return out;
      }

      return exchange.getIn();
   }


}
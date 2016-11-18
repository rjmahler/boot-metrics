package com.joaquin.boot_metrics;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

/**
 * Metrics configuration
 */
@Configuration
public class MetricsConfiguration 
{
	  private static final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);
	
	  @Bean
	  public MetricRegistry metricRegistry() {
	    final MetricRegistry metricRegistry = new MetricRegistry();

	    metricRegistry.register("jvm.memory",new MemoryUsageGaugeSet());
	    metricRegistry.register("jvm.thread-states",new ThreadStatesGaugeSet());
	    metricRegistry.register("jvm.garbage-collector",new GarbageCollectorMetricSet());

	    return metricRegistry;
	  }
	  
	  @Bean
	  @ConditionalOnProperty(prefix = "graphite", name = {"host", "port"})
	  public GraphiteReporter graphiteMetricWriter(@Value("${graphite.host}") String host,
	                                     		@Value("${graphite.port}") int port) {
		  final Graphite graphite = new Graphite(new InetSocketAddress(host, port));
		  final GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry())
		                                                    .prefixedWith("endpoint")
		                                                    .convertRatesTo(TimeUnit.SECONDS)
		                                                    .convertDurationsTo(TimeUnit.MILLISECONDS)
		                                                    .filter(MetricFilter.ALL)
		                                                    .build(graphite);
		  reporter.start(10, TimeUnit.SECONDS);
		  log.info("Graphite timer started on host=" + host + " port=" + port);
		  return reporter;
	  }
}

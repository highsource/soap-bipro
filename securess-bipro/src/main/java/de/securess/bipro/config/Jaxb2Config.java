package de.securess.bipro.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@Configuration
public class Jaxb2Config {
	
	String[] contextPaths = {"net.bipro.namespace.nachrichten","net.bipro.namespace.transfer","net.bipro.namespace.transfer.gevo",
			"net.bipro.namespace.gevo","net.bipro.namespace.prozesse.vertrag","net.bipro.namespace.basis"};
	
	
	@Bean
	public Jaxb2Marshaller createJaxb2Marshaller(@Value("${context.path}") final String contextPath,
												 @Value("${schema.location}") final Resource schemaResource) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		//marshaller.setContextPath(contextPath);
		marshaller.setContextPaths(contextPaths);
		marshaller.setSchemas(
				new ClassPathResource("/xsd/bipro-nachrichten-2.6.0.xsd"),
				new ClassPathResource("/xsd/bipro-transfer-2.6.0.xsd"),
				new ClassPathResource("/xsd/bipro-transfer-gevo-2.6.0.xsd"),
				new ClassPathResource("/xsd/bipro-gevo-2.6.0.xsd"),
				new ClassPathResource("/xsd/bipro-prozesse-vertrag-2.6.0.xsd"),
				new ClassPathResource("xsd/bipro-basis-2.6.0.xsd")
				);
		Map<String, Object> properties = new HashMap<>();
		properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setMarshallerProperties(properties);

		return marshaller;
	}
}

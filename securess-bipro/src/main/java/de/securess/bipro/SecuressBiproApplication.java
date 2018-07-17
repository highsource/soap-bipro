package de.securess.bipro;

import java.io.StringWriter;


import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import de.securess.bipro.tools.CreateBiPRODate;
import net.bipro.namespace.datentypen.STMeldungsart;
import net.bipro.namespace.datentypen.STStatus;
import net.bipro.namespace.gevo.CTGeschaeftsvorgang;
import net.bipro.namespace.nachrichten.CTMeldung;
import net.bipro.namespace.nachrichten.CTStatus;
import net.bipro.namespace.prozesse.vertrag.CTVertragsauskunft;
import net.bipro.namespace.transfer.CTGetShipmentResponse;
import net.bipro.namespace.transfer.CTLieferung;
import net.bipro.namespace.transfer.CTTransfer;
import net.bipro.namespace.transfer.GetShipmentResponse;
import net.bipro.namespace.transfer.ObjectFactory;
import net.bipro.namespace.transfer.gevo.CTGeVoTransfer;


@SpringBootApplication
public class SecuressBiproApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SecuressBiproApplication.class);
	
	@Autowired
	private Jaxb2Marshaller marshaller;

	public static void main(String[] args) {
		SpringApplication.run(SecuressBiproApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		/*
		 * Create an ObjectFactory
		 */
		ObjectFactory objFactory = new ObjectFactory();
		
		/*
		 * Get GetShipmentResponse Object from ObjectFactory
		 * This is the root - Element
		 */
		GetShipmentResponse shipment = objFactory.createGetShipmentResponse();
		
		/*
		 * Create CTGetShipmentResponse Object as Accessory Field
		 */
		CTGetShipmentResponse ctShipment = objFactory.createCTGetShipmentResponse();
		
		/*
		 * Create required element CTStatus for GetShipmentResponse 
		 */
		CTStatus ctStatus = new CTStatus();
		ctStatus.setStatusID(STStatus.OK);
		ctStatus.setProzessID(Long.toString(System.currentTimeMillis()));
		ctStatus.setZeitstempel(CreateBiPRODate.createBiPRODateFromCurrentLocalTime());
		ctStatus.setSchwebe(false);
		
		/*
		 * Create required element CTMeldung for CTStatus
		 */
		CTMeldung ctMeldung = new CTMeldung();
		ctMeldung.setArtID(STMeldungsart.HINWEIS);
		ctMeldung.setMeldungID("04000");
		
		/*
		 * Set CTMeldung in CTStatus
		 */
		ctStatus.getMeldung().add(ctMeldung);
		
		/*
		 * Set CTStatus in GetShipmentResponse
		 */
		ctShipment.setBiPROVersion("2.6.1.0.0");
		ctShipment.setStatus(ctStatus);
		
		/*
		 * Create required field CTLieferung
		 */
		CTLieferung ctLieferung = objFactory.createCTLieferung();
		ctLieferung.setID(Long.toString(System.currentTimeMillis()));
		ctLieferung.setEinstellzeitpunkt(objFactory.createCTLieferungEinstellzeitpunkt("2016-12-05T00:00:00"));
		ctLieferung.setKategorie("120000000");
		ctLieferung.setVerfuegbarBis(objFactory.createCTLieferungVerfuegbarBis("2019-12-05"));

		/*
		 * Create Transfers
		 */
		CTGeVoTransfer ctTransfer = new CTGeVoTransfer();
		ctTransfer.setErstelldatum(null);
		ctTransfer.setSparte(objFactory.createCTTransferSparte("050")); 			
		ctTransfer.getBeitrag().add(null);
		JAXBElement<String> value = new JAXBElement<String>(
				new QName("http://www.bipro.net/namespace/gevo","ArtID"), String.class, "value"
				);
		value.setValue("120000000");
		CTGeschaeftsvorgang ctVertragsauskunft = new CTVertragsauskunft();
		log.info(ctVertragsauskunft.getClass().getSimpleName());
		ctVertragsauskunft.setArtID(value);
		ctTransfer.setArtID(ctVertragsauskunft.getArtID());
		log.info(ctVertragsauskunft.getArtID().getValue());
		ctLieferung.getTransfer().add(ctTransfer);
		ctLieferung.setAnzahlTransfers(1);
		log.info(ctLieferung.getTransfer().get(0).getArtID().getValue());
		
		/*
		 * add JAXBElement<CTLieferung> to CTGetShipmentResponse
		 */
		JAXBElement<CTLieferung> jaxbctLieferung = new JAXBElement<CTLieferung>(
				new QName("http://www.bipro.net/namespace/transfer", "Lieferung"), CTLieferung.class, ctLieferung);
		
		ctShipment.setLieferung(jaxbctLieferung);
		

		
		
		
		shipment.setResponse(ctShipment);
		/*
		 * Create XML String from GetShipmentResponse via JAXBMarshaller.unmarshall Method
		 */
		StringWriter writer = new StringWriter();
		marshaller.marshal(shipment, new StreamResult(writer));
		String xml = writer.toString();
		
		log.info("XML:{}"+xml);
		
		//Source source = new StreamSource(new File("C:\\Users\\jb\\Desktop\\bipro-release-paket-2.6.1\\Nürnberger-BiPRO.xml"));
		//CTLieferung ctLieferung = (CTLieferung) marshaller.unmarshal(source);

		
	}
}

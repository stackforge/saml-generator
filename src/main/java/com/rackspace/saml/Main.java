package com.rackspace.saml;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

public class Main {

	public static void main(String[] args) {
		try {
			HashMap<String, List<String>> attributes = new HashMap<String, List<String>>();
			String issuer = null;
			String subject = null;
			String privateKey = null;
			String publicKey = null;
			Integer samlAssertionExpirationDays = null;
			
			Options options = new Options();
			options.addOption("issuer", true, "Issuer for saml assertion");
			options.addOption("subject", true, "Subject of saml assertion");
            options.addOption("email", true, "Email associated with the subject");
            options.addOption("domain", true, "Domain attribute");
			options.addOption("roles", true, "Comma separated list of roles");
			options.addOption("publicKey", true, "Location of public key to decrypt assertion");
			options.addOption("privateKey", true, "Location or private key use to sign assertion");
			options.addOption("samlAssertionExpirationDays", true, "How long before assertion is no longer valid. Can be negative.");
			
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			if (args.length == 0) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "saml-util-1.0", options, true);
				System.exit(1);
			}
		
			issuer = cmd.getOptionValue("issuer");
			subject = cmd.getOptionValue("subject");
			privateKey = cmd.getOptionValue("privateKey");
			publicKey = cmd.getOptionValue("publicKey");

			samlAssertionExpirationDays = cmd.getOptionValue("samlAssertionExpirationDays") != null ? Integer.valueOf(cmd.getOptionValue("samlAssertionExpirationDays")) : null;
			
			if (cmd.getOptionValue("domain") != null)
				attributes.put("domain", Arrays.asList(cmd.getOptionValue("domain")));
			
			if (cmd.getOptionValue("roles") != null)
				attributes.put("roles", Arrays.asList(cmd.getOptionValue("roles").split(",")));

            if (cmd.getOptionValue("email") != null)
                attributes.put("email", Arrays.asList(cmd.getOptionValue("email")));

			SamlAssertionProducer producer = new SamlAssertionProducer();
			producer.setPrivateKeyLocation(privateKey);
			producer.setPublicKeyLocation(publicKey);
			
			Response responseInitial = producer.createSAMLResponse(subject, new DateTime(), "password", attributes, issuer, samlAssertionExpirationDays);
			
			ResponseMarshaller marshaller = new ResponseMarshaller();
			Element element = marshaller.marshall(responseInitial);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLHelper.writeNode(element, baos);
			String responseStr = new String(baos.toByteArray());
			
			System.out.println(responseStr);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}

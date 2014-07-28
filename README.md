SAML Response Generator
=======================

This is a small utility program that makes it easy to generate SAML responses for testing.

Creating Private and Public Keys for Testing
--------------------------------------------

You will need to generate a private and public key to use for generating saml assertions. The following steps are used for creating the keys:
```
#create the keypair
openssl req -new -x509 -days 3652 -nodes -out saml.crt -keyout saml.pem

#convert the private key to pkcs8 format
openssl pkcs8 -topk8 -inform PEM -outform DER -in saml.pem -out saml.pkcs8 -nocrypt
```

Command line tool
-----------------

You will need to create the jar file in order to use the command line tool. cd to saml-tutorial then run 'mvn package' to create a jar file called 'saml-generator-1.0.jar'. This jar file will be used to create saml assertions.

Usage
-----

java -jar saml-generator-1.0.jar [-domain <arg>] [-issuer <arg>] [-privateKey <arg>] [-publicKey <arg>] [-roles <arg>] [-email <arg>] [-samlAssertionExpirationDays <arg>] [-subject <arg>]

```
-issuer
The URI of the issuer for the saml assertion.

-subject
The username of the federated user.

-domain
The domain ID for the federated user.

-roles
A comma separated list of role names for the federated user.

-email
The email address of the federated user.

-publicKey
THe path to the location of the public key to decrypt assertions

-privateKey
The path to the location of the private key to use to sign assertions

-samlAssertionExpirationDays
How long before the assertion is no longer valid
```

Example
-------

java -jar saml-generator-1.0.jar -domain 7719 -issuer 'http://some.compnay.com' -privateKey saml.pkcs8 -publicKey saml.crt -roles 'role1' -samlAssertionExpirationDays 5 -subject samlUser1

Output:
```
<?xml version="1.0" encoding="UTF-8"?>
<saml2p:Response xmlns:saml2p="urn:oasis:names:tc:SAML:2.0:protocol" xmlns:xs="http://www.w3.org/2001/XMLSchema" ID="e1af8c40-8b45-4f25-a8c5-fd99df001c0e" IssueInstant="2014-06-17T20:47:33.381Z" Version="2.0">
  <saml2:Issuer xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion">http://test.rackspace.com</saml2:Issuer>
  <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
    <ds:SignedInfo>
      <ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
      <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
      <ds:Reference URI="#e1af8c40-8b45-4f25-a8c5-fd99df001c0e">
        <ds:Transforms>
          <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
          <ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
            <ec:InclusiveNamespaces xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="xs"/>
          </ds:Transform>
        </ds:Transforms>
        <ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
        <ds:DigestValue>fufQ5g8YHPZVT4tX6Xx4LfYO588=</ds:DigestValue>
      </ds:Reference>
    </ds:SignedInfo>
    <ds:SignatureValue>LlYniaVX8EXAZDvKP396IDpDm31mJf3T8HKh4NroTSPWqEjmcN2Wj32QBjSCpzXtE7bhVoRIQQRDRWzAbMjR0gjuy6NK0z1vBQDi4iwuRM6Y+sgsDAqB9wT4h4yi6J7cjnUdNi83VRVYF3F7zVjCq//mDQVkyp+rkhC0Lkxe2kM=</ds:SignatureValue>
  </ds:Signature>
  <saml2p:Status>
    <saml2p:StatusCode Value="urn:oasis:names:tc:SAML:2.0:status:Success"/>
  </saml2p:Status>
  <saml2:Assertion xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion" ID="4ee2be6a-8075-40a2-ba89-cf0991880af2" IssueInstant="2014-06-17T20:47:33.379Z" Version="2.0">
    <saml2:Issuer>http://some.compnay.com</saml2:Issuer>
    <saml2:Subject>
      <saml2:NameID Format="urn:oasis:names:tc:SAML:2.0:nameid-format:persistent">samlUser</saml2:NameID>
      <saml2:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
        <saml2:SubjectConfirmationData NotOnOrAfter="2014-06-22T20:47:33.373Z"/>
      </saml2:SubjectConfirmation>
    </saml2:Subject>
    <saml2:AuthnStatement AuthnInstant="2014-06-17T20:47:31.963Z">
      <saml2:AuthnContext>
        <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport</saml2:AuthnContextClassRef>
      </saml2:AuthnContext>
    </saml2:AuthnStatement>
    <saml2:AttributeStatement>
      <saml2:Attribute Name="roles">
        <saml2:AttributeValue xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xs:string">role1</saml2:AttributeValue>
      </saml2:Attribute>
      <saml2:Attribute Name="domain">
        <saml2:AttributeValue xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xs:string">14309</saml2:AttributeValue>
      </saml2:Attribute>
    </saml2:AttributeStatement>
  </saml2:Assertion>
</saml2p:Response>
```

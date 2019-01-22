package com.bbz.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.*;
import java.nio.charset.Charset;

@Service
public class IdenticonProvider {

    private final Logger logger = LoggerFactory.getLogger(IdenticonProvider.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public InputStream getIdenticonInputStreamFromRest (String value) {
        String identiconStrFromRest = getIdenticonStrFromRest(value);
        InputStream inputStream = new ByteArrayInputStream(identiconStrFromRest.getBytes(Charset.forName("UTF-8")));
        return inputStream;
    }

    private String getIdenticonStrFromRest(String value) {
       value = value.replace("}","")
               .replace("{","");

        String imageUrl = "https://avatars.dicebear.com/v2/identicon/:"+value+".svg";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(imageUrl, String.class);
            if(!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new UnknownHttpStatusCodeException(response.getStatusCode().value(),response.getStatusCode().toString(),response.getHeaders(),null,null);
            }
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
            logger.debug(e.getMessage());
            logger.debug("Could not get identicon from "+imageUrl+" Using default instead");
            return getIdenticonStrFromLocal();
        }
    }


    private String getIdenticonStrFromLocal() {
        try {
            String localImageUrl = "/default_identicon.svg";
            Class clazz = IdenticonProvider.class;
            InputStream inputStream = clazz.getResourceAsStream(localImageUrl);
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            logger.debug("Could not get identicon from local file, returning hardcoded. " +e.getMessage());
            return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" viewBox=\"0 0 5 5\" version=\"1.1\" shape-rendering=\"crispEdges\"><path d=\"M0 0h5v5H0V0z\" fill=\"#ffffff\"/><path d=\"M1 0h3v1H1V0z\" fill=\"#4fc3f7\"/><path d=\"M1 1h3v1H1V1z\" fill=\"#4fc3f7\"/><path d=\"M1 2h3v1H1V2z\" fill=\"#4fc3f7\"/><path d=\"M0 3h2v1H0V3zm3 0h2v1H3V3z\" fill-rule=\"evenodd\" fill=\"#4fc3f7\"/><path d=\"M0 4h2v1H0V4zm3 0h2v1H3V4z\" fill-rule=\"evenodd\" fill=\"#4fc3f7\"/></svg>";
        }
    }
}
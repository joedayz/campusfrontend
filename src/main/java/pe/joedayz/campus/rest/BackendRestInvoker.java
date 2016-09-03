package pe.joedayz.campus.rest;


import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import pe.joedayz.campus.exception.GenericRestException;
import pe.joedayz.campus.util.RestUtil;

/**
 * Created by awusr on 04/05/2016.
 */
//@RestController
public class BackendRestInvoker<T> {

    private static final Logger log = LoggerFactory.getLogger(BackendRestInvoker.class);
    
	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

    private static String SESSION_USER_NAME="SESSION_USER_NAME";

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public BackendRestInvoker(String serverName, int port){
        server=serverName;
        this.port=port;
    }

    public ResponseEntity<T> sendPost(String prUrl,Object requestObject,ParameterizedTypeReference typeRef) {
        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
        HttpHeaders headers=configHttpHeader();


        URI uri = null;
        try {
            uri = new URI(getBackenPath()+prUrl);
            HttpEntity<?> requestEntity = new HttpEntity<>(requestObject,headers);

            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, typeRef);


            return responseEntity;
        } catch (URISyntaxException e) {
            //TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }

        return null;

    }

    public ResponseEntity<T> sendPost(String prUrl,Object requestObject,Class<T> typeParameterClass) {
        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
        HttpHeaders headers=configHttpHeader();


        URI uri = null;
        try {
            uri = new URI(getBackenPath()+prUrl);
            HttpEntity<?> requestEntity = new HttpEntity<>(requestObject,headers);

            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, typeParameterClass);


            return responseEntity;
        } catch (URISyntaxException e) {
            //TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }

        return null;

    }

    public T post(String prUrl,Object requestObject,Class<T> typeParameterClass) {
        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
        HttpHeaders headers=configHttpHeader();


        URI uri = null;
        try {
            uri = new URI(getBackenPath()+prUrl);
            HttpEntity<?> requestEntity = new HttpEntity<>(requestObject,headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            if (RestUtil.isError(responseEntity.getStatusCode()) ) {
                VndErrors vndErrors=OBJECT_MAPPER.readValue(responseEntity.getBody(), VndErrors.class);
                Iterator<VndErrors.VndError> errors=vndErrors.iterator();
                String message="";
                while (errors.hasNext() ) {
                    VndErrors.VndError error=errors.next();
                    if(!"".equals(message)){
                        message=message+";";
                    }
                    message=message+error.getMessage();
                }

                throw new GenericRestException("ERROR",message );
            } else {
                OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                if(responseEntity.getBody()==null){

                    return   (T)responseEntity;

                }
                T responseBody = OBJECT_MAPPER.readValue(responseEntity.getBody(), typeParameterClass);
                return responseBody;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GenericRestException("ERROR",e.getMessage() );
        }

//        return null;

    }


    public ResponseEntity<T> sendGet(String prUrl,Class<T> typeParameterClass) {
        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(new MediaType("application","json")));
        HttpHeaders headers=configHttpHeader();
        URI uri= null;
        try {
            uri = new URI(getBackenPath()+prUrl);
            HttpEntity requestEntity = new HttpEntity(headers);
            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, typeParameterClass);


            return responseEntity;
        } catch (URISyntaxException e) {
            //TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }


        return null;

    }


    public ResponseEntity<List<T>> sendGetList(String prUrl,Object requestObject,Class<T> typeParameterClass) {

        ParameterizedTypeReference<List<T>> typeRef=new ParameterizedTypeReference<List<T>>() {


        };

        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        //HttpHeaders headers = new HttpHeaders();
        HttpHeaders headers=configHttpHeader();
        //headers.setAccept(Collections.singletonList(new MediaType("application","json")));

        String queryParams=toQueryParams(requestObject);


        URI uri= null;
        try {
//            uri = new URI(prUrl+queryParams);
            uri = new URI(getBackenPath()+prUrl+queryParams);
            //uri = new URI(backendUrl+prUrl+queryParams);
            HttpEntity requestEntity = new HttpEntity(headers);

            ResponseEntity<List<T>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, typeRef);

            return responseEntity;
        } catch (URISyntaxException e) {
//TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }


        return null;

    }

    public ResponseEntity<T> sendGet(String prUrl,ParameterizedTypeReference typeRef)  {

        RestTemplate restTemplate = RestTemplateFactory.restTemplate();

        HttpHeaders headers=configHttpHeader();


        URI uri= null;
        try {

            uri = new URI(getBackenPath()+prUrl);
            HttpEntity requestEntity = new HttpEntity(headers);

            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, typeRef);

            return responseEntity;
        } catch (URISyntaxException e) {
//TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }


        return null;

    }


    public ResponseEntity<T> sendGet(String prUrl,Object requestObject,ParameterizedTypeReference typeRef)  {

        RestTemplate restTemplate = RestTemplateFactory.restTemplate();
        HttpHeaders headers=configHttpHeader();

        String queryParams=toQueryParams(requestObject);

        URI uri= null;
        try {
            uri = new URI(getBackenPath()+prUrl+queryParams);
            HttpEntity requestEntity = new HttpEntity(headers);

            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, typeRef);

            return responseEntity;
        } catch (URISyntaxException e) {
//TODO lanzar excepcion
            //throw new Exception(e);
            log.error(e.getMessage(), e);
        }


        return null;

    }

    private HttpHeaders configHttpHeader( ){
        HttpHeaders headers = new HttpHeaders();

       List<MediaType> acceptedMediaTypes= new ArrayList<MediaType>();
        acceptedMediaTypes.add(new MediaType("application","json"));
        headers.setAccept(Collections.singletonList(new MediaType("application","json")));
        return headers;
    }

    private String getBackenPath(){
        return "http://"+server+":"+port;
    }

    private String toQueryParams(Object requestObject){
        StringBuilder queryParams= new StringBuilder();

        Field[] fields= requestObject.getClass().getDeclaredFields();
        for (Field f: fields){
            try {
                f.setAccessible(true);
                Object value=f.get(requestObject);
                if(value!=null && !(value instanceof List) ){
                    if(queryParams.length()==0)queryParams.append("?");
                    else queryParams.append("&");
                    queryParams.append(f.getName()).append("=").append(value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        String params=queryParams.toString();

        params=params.replaceAll(" ", "%20");
        return params;
    }

  public byte[] sendGetFile(String prUrl, Object requestObject)  {

        RestTemplate restTemplate = RestTemplateFactory.restFilesTemplate();
         URI uri= null;
        String queryParams="";
            if(requestObject!=null){
                queryParams=toQueryParams(requestObject);
            }
        try {
            uri = new URI(getBackenPath()+prUrl+queryParams);

            return restTemplate.execute(uri, HttpMethod.GET, null,
                    new ResponseExtractor<byte[]>() {
                        @Override
                        public byte[] extractData(ClientHttpResponse response) throws IOException {
                             byte[] bytes = IOUtils.toByteArray(response.getBody());
                             return bytes;
                       }
                    });
        } catch (URISyntaxException e) {
          //e.printStackTrace();
          log.error(e.getMessage(), e);
        }

        return null;
    }


}

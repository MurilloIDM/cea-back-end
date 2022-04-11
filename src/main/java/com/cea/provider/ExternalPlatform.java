package com.cea.provider;

import com.cea.dto.externalPlatform.*;
import com.cea.utils.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExternalPlatform {
    private static String TOKEN = null;
    private static Integer QUERY_ATTEMPTS = 0;

    private final ConfigProperties configProperties;

    private String auth() {
        String BASE_URL = configProperties.getProperty("externalPlatform.url");
        String uri = String.format("%s/credential/generate_token", BASE_URL);

        String EP_EMAIL = configProperties.getProperty("externalPlatform.email");
        String EP_APIKEY = configProperties.getProperty("externalPlatform.apikey");
        String EP_PUBLICKEY = configProperties.getProperty("externalPlatform.publickey");

        AuthDTO payload = new AuthDTO(EP_EMAIL, EP_PUBLICKEY, EP_APIKEY);

        RestTemplate restTemplate = new RestTemplate();
        ResponseAuthDTO result = restTemplate.postForObject(uri, payload, ResponseAuthDTO.class);

        String token = null;

        if (result.getSuccess()) {
            token = result.getData().getToken();
        }

        return token;
    }

    public StudentInPlatformDTO isStudentInPlatform(String email) {
        String BASE_URL = configProperties.getProperty("externalPlatform.url");
        boolean isStudent = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        if (TOKEN == null) {
            TOKEN = this.auth();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        String page = "1";
        String startDate = "2021-01-01";
        String endDate = dateFormat.format(new Date());
        String contentId = configProperties.getProperty("externalPlatform.contentid");

        String uri = String.format("%s/sale/get_sale_list", BASE_URL);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("start_date", "{start_date}")
                .queryParam("end_date", "{end_date}")
                .queryParam("page", "{page}")
                .queryParam("client_email", "{client_email}")
                .queryParam("content_id", "{content_id}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("page", page);
        params.put("client_email", email);
        params.put("content_id", contentId);

        ResponseEntity<ClientDTO> result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.exchange(urlTemplate,
                    HttpMethod.GET,
                    entity,
                    ClientDTO.class,
                    params);
        } catch (RestClientResponseException error) {
            Integer status = error.getRawStatusCode();

            if (status == 401 && QUERY_ATTEMPTS < 3) {
                TOKEN = null;
                QUERY_ATTEMPTS += 1;
                this.isStudentInPlatform(email);
            }

            throw new HttpClientErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro na verificação na plataforma externa.");
        }

        List<ResponseDataClientDTO> studentRecord = result.getBody().getData();

        ResponseDataClientDTO studentInPlatform = null;
        for (ResponseDataClientDTO student : studentRecord) {
            if (student.getSale_status_name().equalsIgnoreCase("Paga")) {
                isStudent = true;
                studentInPlatform = student;
                break;
            }
        }

        QUERY_ATTEMPTS = 0;
        return new StudentInPlatformDTO(isStudent, studentInPlatform);
    }

}

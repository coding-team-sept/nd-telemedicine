package com.github.coding_team_sept.nd_backend.chat.services;

import com.github.coding_team_sept.nd_backend.chat.exceptions.RestClientException;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.AppointmentsResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.PatientAppointmentResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public record AppointmentService(
        RestTemplate restTemplate
) {
    public static final String url = "http://appointment:9001/api/v1";

    public DoctorAppointmentResponse getDoctorAppointment(
            HttpHeaders headers,
            Long id) throws RestClientException {
        String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/doctor/appointment")
                .queryParam("id", id)
                .encode()
                .toUriString();

        try {
            // Check "Authorization"
            final var response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseWrapper<AppointmentsResponse<DoctorAppointmentResponse>>>() {
                    });

            if (response.getBody() != null) {
                return response.getBody().data.appointments.get(0);
            }
            throw RestClientException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response body");
        } catch (RestClientResponseException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
    }

    public PatientAppointmentResponse getPatientAppointment(
            HttpHeaders headers,
            Long id) throws RestClientException {
        String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/patient/appointment")
                .queryParam("id", id)
                .encode()
                .toUriString();

        try {
            // Check "Authorization"
            final var response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseWrapper<AppointmentsResponse<PatientAppointmentResponse>>>() {
                    });

            if (response.getBody() != null) {
                return response.getBody().data.appointments.get(0);
            }
            throw RestClientException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response body");
        } catch (RestClientResponseException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
    }
}

package com.aias.springboot.test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> ResponseUtil <br>
 * <b>@Date:</b> 2019/10/30 <br>
 *
 * @author <a> liuhy </a><br>
 */
public class ResponseUtil {
	private ResponseUtil() {
	}

	public static <X> ResponseEntity<X> wrapOrNotFound(
			Optional<X> maybeResponse) {
		return wrapOrNotFound(maybeResponse, (HttpHeaders) null);
	}

	public static <X> ResponseEntity<X> wrapOrNotFound(
			Optional<X> maybeResponse, HttpHeaders header) {
		return (ResponseEntity) maybeResponse.map((response) -> {
			return ((ResponseEntity.BodyBuilder) ResponseEntity.ok()
					.headers(header)).body(response);
		}).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
	}

	public static <X> ResponseEntity<X> badResponse(HttpHeaders headers) {
		return badResponse(headers, HttpStatus.BAD_REQUEST);
	}

	public static <X> ResponseEntity<X> badResponse(HttpHeaders headers,
			HttpStatus status) {
		return (ResponseEntity<X>) ((ResponseEntity.BodyBuilder) ResponseEntity
				.status(status).headers(headers)).body((Object) null);
	}
}

package com.example.tareaspring.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class ApiError {

    @NonNull
    private HttpStatus status;
    @NonNull
    private LocalDateTime timestamp;
    @NonNull
    private Map<String, String> errors;
}

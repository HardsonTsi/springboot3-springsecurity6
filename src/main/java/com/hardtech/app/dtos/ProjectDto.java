package com.hardtech.app.dtos;

import java.time.LocalDate;

public record ProjectDto(String name, String description, LocalDate startAt, LocalDate endAt) {
}

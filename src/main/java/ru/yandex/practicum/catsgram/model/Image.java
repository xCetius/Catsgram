package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"id"})
@Data
public class Image {
    Long id;
    long postId;
    String originalFileName;
    String filePath;
}

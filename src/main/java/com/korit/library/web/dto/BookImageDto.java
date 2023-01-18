package com.korit.library.web.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookImageDto {

    private int imageId;

    private String bookCode;

    private String saveName;

    private String originName;


}

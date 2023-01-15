package com.korit.library.web.dto;
//요청 DTO

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class BookReqDto {
    @ApiModelProperty(value = "도서코드", example = "소록-999",required = true)
    @NotBlank //유효성 검사 required = true 에 유효성 검사 NotBlank
    private String bookCode;

    @NotBlank
    @ApiModelProperty(value = "도서명", example = "테스트 도서명" ,required = true)
    private String bookName;

    @ApiModelProperty(value = "저자" , example = "테스터")
    private String author;

    @ApiModelProperty(value = "출판사" ,example = "테스트 출판사")
    private String publisher;

    @ApiModelProperty(value = "출판일" ,example = "2023-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    //JSON 데이터로 들어오면 LocalDate로 날짜를 자동으로 형변환해서 넣어줌
    private LocalDate publicationDate;

    @NotBlank
    @ApiModelProperty(value = "카테고리",example = "가정/생활",required = true)
    private String category;
}

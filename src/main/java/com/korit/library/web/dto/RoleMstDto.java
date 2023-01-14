package com.korit.library.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoleMstDto {
    private int roleId;
    private String roleName;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}

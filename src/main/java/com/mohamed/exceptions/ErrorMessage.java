package com.mohamed.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public  class ErrorMessage {

    private  String error_Message;
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss" ,timezone = "Europe/Berlin")
    private Date error_Date;


}

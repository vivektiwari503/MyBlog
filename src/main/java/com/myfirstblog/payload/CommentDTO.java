package com.myfirstblog.payload;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;
    private String body;
    private String name;
    private String email;
}


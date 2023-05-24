package com.myfirstblog.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
    @Entity
    @Table(name = "comments")
    public class Comment {//Many

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String body;
        private String name;
        private String email;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", nullable = false)
        private Post post;//one that's why we did not use list here


    }



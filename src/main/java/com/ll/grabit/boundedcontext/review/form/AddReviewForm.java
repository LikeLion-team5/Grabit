package com.ll.grabit.boundedcontext.review.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewForm {
    private int rating;
    private String content;
}


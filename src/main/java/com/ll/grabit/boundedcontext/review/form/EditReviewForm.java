package com.ll.grabit.boundedcontext.review.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditReviewForm {
    private int rating;
    private String content;
}

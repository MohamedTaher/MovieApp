package com.example.taher.movieapp;

import java.io.Serializable;

/**
 * Created by taher on 13/08/16.
 */
public class ReviewItem  implements Serializable {
    private String name;
    private String review;

    public ReviewItem(String name, String review) {
        this.name = name;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

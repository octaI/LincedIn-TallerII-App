package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserRecommendation implements Serializable {
    private String recommender;
    private String text;

    public UserRecommendation() {}

    public UserRecommendation(String recommender, String text) {
        this.recommender = recommender;
        this.text = text;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRecommendation that = (UserRecommendation) o;

        if (recommender != null ? !recommender.equals(that.recommender) : that.recommender != null)
            return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        int result = recommender != null ? recommender.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

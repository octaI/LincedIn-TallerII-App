package com.fiuba.tallerii.lincedin.model.recommendations;

import com.google.gson.annotations.SerializedName;

public class RecommendationSent {

    public String text;

    @SerializedName("user_id")
    public String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendationSent that = (RecommendationSent) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}

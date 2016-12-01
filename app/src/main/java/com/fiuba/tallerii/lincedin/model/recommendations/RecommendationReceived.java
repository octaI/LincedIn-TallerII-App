package com.fiuba.tallerii.lincedin.model.recommendations;

public class RecommendationReceived {

    public String text;

    public String recommender;

    public String timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendationReceived that = (RecommendationReceived) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (recommender != null ? !recommender.equals(that.recommender) : that.recommender != null)
            return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (recommender != null ? recommender.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}

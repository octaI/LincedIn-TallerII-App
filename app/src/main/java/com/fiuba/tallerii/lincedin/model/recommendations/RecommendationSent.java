package com.fiuba.tallerii.lincedin.model.recommendations;

import com.google.gson.annotations.SerializedName;

public class RecommendationSent {

    public String text;

    @SerializedName("user_id")
    public String userId;
}

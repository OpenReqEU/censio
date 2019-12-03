package com.selectionarts.projectcensio.model.rating;


import lombok.Data;

@Data
public class ThumbsRating extends Rating {

    public enum Thumbs {
        ACCEPT(1),
        DISAGREE(-1),
        STAYOUT(0);

        private final int levelCode;

        private Thumbs(int levelCode) {
            this.levelCode = levelCode;
        }

        public int getLevelCode() {
            return levelCode;
        }
    }

    private int rating;

    public ThumbsRating(Thumbs thumbs)
    {
        this.rating = thumbs.levelCode;
    }
}

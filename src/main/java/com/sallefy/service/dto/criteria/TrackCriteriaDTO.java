package com.sallefy.service.dto.criteria;

import java.io.Serializable;
import java.util.Objects;

public class TrackCriteriaDTO extends BaseCriteria implements Serializable {

    private Boolean recent;

    private Boolean liked;

    private Boolean played;

    public TrackCriteriaDTO() {
    }

    public Boolean getRecent() {
        return recent;
    }

    public void setRecent(Boolean recent) {
        this.recent = recent;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackCriteriaDTO)) return false;
        TrackCriteriaDTO that = (TrackCriteriaDTO) o;
        return Objects.equals(recent, that.recent) &&
            Objects.equals(liked, that.liked) &&
            Objects.equals(played, that.played);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recent, liked, played);
    }

    @Override
    public String toString() {
        return "TrackCriteria{" +
            "recent=" + recent +
            ", liked=" + liked +
            ", played=" + played +
            '}';
    }
}

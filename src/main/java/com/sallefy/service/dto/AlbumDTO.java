package com.sallefy.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sallefy.domain.Album} entity.
 */
public class AlbumDTO implements Serializable {

    private Long id;

    private String title;

    private String reference;

    private Integer totalTracks;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlbumDTO albumDTO = (AlbumDTO) o;
        if (albumDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), albumDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlbumDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", reference='" + getReference() + "'" +
            ", totalTracks=" + getTotalTracks() +
            "}";
    }
}

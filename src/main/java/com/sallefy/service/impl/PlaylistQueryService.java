package com.sallefy.service.impl;

import com.sallefy.domain.*;
import com.sallefy.repository.PlaylistRepository;
import com.sallefy.service.QueryService;
import com.sallefy.service.UserService;
import com.sallefy.service.dto.PlaylistDTO;
import com.sallefy.service.dto.criteria.PlaylistCriteriaDTO;
import com.sallefy.service.mapper.PlaylistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.SetJoin;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.criteria.JoinType.INNER;
import static org.springframework.data.domain.PageRequest.of;

/**
 * Service for executing complex queries for {@link Playlist} entities in the database.
 * The main input is a {@link PlaylistCriteriaDTO} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlaylistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaylistQueryService implements QueryService<PlaylistDTO, PlaylistCriteriaDTO> {

    private final Logger log = LoggerFactory.getLogger(PlaylistQueryService.class);

    private final PlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;

    private final UserService userService;

    public PlaylistQueryService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper, UserService userService) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link PlaylistDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findByCriteria(PlaylistCriteriaDTO criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Playlist> specification = createSpecification(criteria);

        List<Playlist> playlists;

        if (isSizeSelected(criteria)) {
            playlists = playlistRepository.findAll(specification, of(0, criteria.getSize())).getContent();
        } else {
            playlists = playlistRepository.findAll(specification);
        }

        return transformPlaylists(playlists);
    }

    /**
     * Function to convert {@link PlaylistCriteriaDTO} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Playlist> createSpecification(PlaylistCriteriaDTO criteria) {
        Specification<Playlist> specification = Specification.where(null);

        final User user = userService.getUserWithAuthorities();

        if (criteria != null) {
            if (criteria.isRecent() != null) {
                specification = specification.and(sortByCreated());
            }
            if (criteria.getPopular() != null) {
                specification = specification.and(sortByMostFollowed());
            }
            if (!user.isAdmin()) {
                specification = specification.and(findPublicPlaylists());
            }
        }
        return specification;
    }

    private Specification<Playlist> findPublicPlaylists() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get(Playlist_.publicAccessible), true);
    }

    private Specification<Playlist> sortByMostFollowed() {

        return (root, query, builder) -> {
            SetJoin<Playlist, FollowPlaylist> followPlaylist = root.join(Playlist_.followers, INNER);
            query.groupBy(followPlaylist.get(FollowPlaylist_.playlist));

            final Order order = builder.desc(builder.count(followPlaylist.get(FollowPlaylist_.id)));

            return query.orderBy(order).getRestriction();
        };
    }

    private Specification<Playlist> sortByCreated() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final Order order = criteriaBuilder.desc(root.get(Playlist_.created));
            return criteriaQuery.orderBy(order).getRestriction();
        };
    }

    private List<PlaylistDTO> transformPlaylists(List<Playlist> playlists) {
        return playlists.stream()
            .map(playlistMapper::toDto)
            .collect(Collectors.toList());
    }

    private boolean isSizeSelected(PlaylistCriteriaDTO criteria) {
        return criteria.getSize() != null;
    }
}

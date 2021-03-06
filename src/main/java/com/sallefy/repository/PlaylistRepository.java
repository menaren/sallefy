package com.sallefy.repository;

import com.sallefy.domain.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Playlist entity.
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long>, JpaSpecificationExecutor<Playlist> {

    @Query("select playlist from Playlist playlist where playlist.user.login = ?#{principal.username}")
    List<Playlist> findByUserIsCurrentUser();

    @Query(value = "select distinct playlist from Playlist playlist left join fetch playlist.tracks",
        countQuery = "select count(distinct playlist) from Playlist playlist")
    Page<Playlist> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct playlist from Playlist playlist left join fetch playlist.tracks")
    List<Playlist> findAllWithEagerRelationships();

    @Query("select distinct playlist from Playlist playlist left join fetch playlist.tracks where playlist.publicAccessible = true")
    List<Playlist> findAllWithEagerRelationshipsAndPublicAccessibleTrue();

    @Query("select playlist from Playlist playlist left join fetch playlist.tracks where playlist.id =:id")
    Optional<Playlist> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select playlist from Playlist playlist where playlist.user.login = ?#{principal.username} and playlist.id = :id")
    Optional<Playlist> findByUserIsCurrentUserAndId(@Param("id") Long id);

    @Query("select playlist from Playlist playlist where playlist.user.login = :login")
    List<Playlist> findAllByUserLogin(@Param("login") String login);

    @Query("select playlist from Playlist playlist where playlist.user.login = :login and playlist.publicAccessible = true")
    List<Playlist> findAllByUserLoginAndPublicAccessibleTrue(@Param("login") String login);
}

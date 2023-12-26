package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Favorites;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorites, Long> {
    Favorites save(Favorites favorites);

    @Query("SELECT f FROM Favorites f WHERE f.user_id = :userId AND f.favorite_id = :favoriteUserId")
    Favorites findByUserIdAndFavoriteId(Long userId, Long favoriteUserId);

    @Query("SELECT f FROM Favorites f JOIN User u ON f.favorite_id = u.id JOIN u.roles ur ON ur.name = 'MENTEE' WHERE f.user_id = :userId")
    List<Favorites> findAllFavoriteMenteeById(@Param("userId") Long userId);

    @Query("SELECT f FROM Favorites f JOIN User u ON f.favorite_id = u.id JOIN u.roles ur ON ur.name = 'MENTOR' WHERE f.user_id = :userId")
    List<Favorites> findAllFavoriteMentorById(@Param("userId") Long userId);
}

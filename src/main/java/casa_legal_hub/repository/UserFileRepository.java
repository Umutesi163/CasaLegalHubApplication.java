package casa_legal_hub.repository;

import casa_legal_hub.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    // Exclude fileData from list queries for performance
    @Query("SELECT f FROM UserFile f JOIN FETCH f.owner WHERE f.owner.username = :username ORDER BY f.uploadedAt DESC")
    List<UserFile> findByOwnerUsernameOrderByUploadedAtDesc(String username);

    @Query("SELECT f FROM UserFile f JOIN FETCH f.owner WHERE f.owner.username = :username AND f.category = :category ORDER BY f.uploadedAt DESC")
    List<UserFile> findByOwnerUsernameAndCategoryOrderByUploadedAtDesc(String username, String category);

    Optional<UserFile> findByIdAndOwnerUsername(Long id, String username);
}

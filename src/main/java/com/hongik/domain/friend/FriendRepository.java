package com.hongik.domain.friend;

import com.hongik.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	Friend findBySenderAndReceiver(User sender, User receiver);

	Optional<Friend> findBySenderAndReceiverAndFriendStatus(User sender, User receiver, FriendStatus friendStatus);

	@Query("""
    SELECT f FROM Friend f
    WHERE (f.sender.id = :userId AND f.receiver.id = :friendId)
       OR (f.sender.id = :friendId AND f.receiver.id = :userId)
    """)
	Optional<Friend> findFriendRelation(Long userId, Long friendId);
}

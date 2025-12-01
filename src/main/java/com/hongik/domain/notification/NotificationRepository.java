package com.hongik.domain.notification;

import com.hongik.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByReceiverIdAndIsReadOrderByCreatedAtDesc(Long receiverId, boolean isRead);

	Optional<Notification> findTopByFriendIdOrderByCreatedAtDesc(Long friendId);
}

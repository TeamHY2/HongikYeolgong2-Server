package com.hongik.domain.friend;

import com.hongik.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	Friend findBySenderAndReceiver(User sender, User receiver);
}

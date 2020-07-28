package com.win.twitter.repository;

//This interacts with the database and allows us to convert between tweet objects in our code and rows in the tweet table.
import java.util.List;

import com.win.twitter.model.Tweet;
import com.win.twitter.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {
    List<Tweet> findAllByOrderByCreatedAtDesc();

    List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);

    List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);
}
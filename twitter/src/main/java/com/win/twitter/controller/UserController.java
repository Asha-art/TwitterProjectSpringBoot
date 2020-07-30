package com.win.twitter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.win.twitter.model.Tweet;
import com.win.twitter.model.TweetDisplay;
import com.win.twitter.model.User;
import com.win.twitter.service.TweetService;
import com.win.twitter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @GetMapping(value = "/users/{username}")

    public String getUser(@PathVariable(value = "username") String username, Model model) {
        User user = userService.findByUsername(username);
        List<TweetDisplay> tweets = tweetService.findAllByUser(user);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("user", user);
        User loggedInUser = userService.getLoggedInUser();
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;
        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }
        model.addAttribute("following", isFollowing);
        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);
        return "user";

    }

    // create a page that shows a list of all users
    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(value = "filter", required = false) String filter, Model model) {
        List<User> users = new ArrayList<User>();
        // To get the list of the user's followers, in addition to the users they're
        // following
        User loggedInUser = userService.getLoggedInUser();

        List<User> usersFollowing = loggedInUser.getFollowing();
        List<User> usersFollowers = loggedInUser.getFollowers();
        if (filter == null) {
            filter = "all";
        }
        // for only getting list of followers
        if (filter.equalsIgnoreCase("followers")) {
            users = usersFollowers;
            model.addAttribute("filter", "followers");
        } else if (filter.equalsIgnoreCase("following")) { // the case for only getting the users that the user is
                                                           // following:
            users = usersFollowing;
            model.addAttribute("filter", "following");
        } else { // the case for getting all user
            users = userService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("users", users);

        SetTweetCounts(users, model);
        SetFollowingStatus(users, usersFollowing, model);

        return "users";
    }

    private void SetTweetCounts(List<User> users, Model model) {
        HashMap<String, Integer> tweetCounts = new HashMap<>();
        for (User user : users) {
            List<TweetDisplay> tweets = tweetService.findAllByUser(user);
            tweetCounts.put(user.getUsername(), tweets.size());
        }
        model.addAttribute("tweetCounts", tweetCounts);
    }

    private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String, Boolean> followingStatus = new HashMap<>();
        String username = userService.getLoggedInUser().getUsername();
        for (User user : users) {
            if (usersFollowing.contains(user)) {
                followingStatus.put(user.getUsername(), true);
            } else if (!user.getUsername().equals(username)) {
                followingStatus.put(user.getUsername(), false);
            }
        }
        model.addAttribute("followingStatus", followingStatus);
    }

}
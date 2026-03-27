package service;
import domain.*;
import observer.*;
import observer.Observable;
import repo.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import observer.Observer;


public class SocialService implements Observable {
    private UserRepo userRepo;
    private TopicRepo topicRepo;
    private PostRepo postRepo;

    private final List<Observer> observers = new ArrayList<>();
    private static final Pattern HASHTAG = Pattern.compile("#(\\w+)");
    private static final Pattern MENTION = Pattern.compile("@(\\w+)");

    public SocialService(UserRepo ur, TopicRepo tr, PostRepo pr){
        this.userRepo=ur; this.topicRepo=tr; this.postRepo=pr;
    }
    @Override
    public void addObserver(Observer observer) {observers.add(observer);}

    @Override
    public void removeObserver(Observer observer) {observers.remove(observer);}

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public List<Post> getMyPosts(User u) throws Exception{
        return postRepo.findByUserDesc(u.id());
    }

    public List<Topic> getMyTopics(User u) throws Exception{
        return topicRepo.findSubscribedTopics(u.id());
    }

    public List<Topic> searchTopics(String text) throws Exception{
        if (text == null) text="";
        return topicRepo.findBySubstring(text);
    }
    public void subscribe(User u, Topic t) throws Exception{
        topicRepo.subscribe(u.id(),t.id());
        notifyObservers();
    }
    private static List<String> extract(Pattern pattern, String text){
        List<String> out = new ArrayList<>();
        Matcher m = pattern.matcher(text);
        while(m.find()) out.add(m.group(1));
        return out;
    }
    public List<Post> getFeedFor(User u) throws Exception{
        var subscribed = getMyTopics(u).stream()
                .map(t->t.name().toLowerCase())
                .collect(Collectors.toSet());

        var allPosts = postRepo.findAllDesc();
        List<Post> feed = new ArrayList<>();
        for (Post p:allPosts){
                String text = p.text();
                boolean okHashtag = extract(HASHTAG,text).stream()
                        .map(String::toLowerCase)
                        .anyMatch(subscribed::contains);

                boolean okMention = extract(MENTION,text).stream()
                        .anyMatch(name->name.equalsIgnoreCase(u.name()));
                if (okHashtag || okMention) feed.add(p);
        }
        return feed;

    }

    public void addPost(User u, String text) throws Exception{
        if (text == null || text.trim().length()<3) throw new IllegalArgumentException("Post too short");
        postRepo.add(u.id(),text.trim(),LocalDateTime.now());
        notifyObservers();
    }
    public void updatePostText(int postId, String newText) throws Exception {
        if (newText == null || newText.trim().length()<3) throw new IllegalArgumentException("Post too short");
        postRepo.updateText(postId,newText);
        notifyObservers();

    }




}

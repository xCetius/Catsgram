package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    public List<Post> findAll(int size, int from, String sort) {
        List<Post> result = new ArrayList<>(posts.values());
        if (sort.equals("asc") || sort.equals("ascending")) {
            result.sort((o1, o2) -> o1.getPostDate().compareTo(o2.getPostDate()));
        } else {
            result.sort((o1, o2) -> o2.getPostDate().compareTo(o1.getPostDate()));
        }

        if (from > 0 && (size > 0 && size <= result.size())) {
            result = new ArrayList<>(result.subList(from, size));
        } else if (size > 0 && size <= result.size()) {
            result = new ArrayList<>(result.subList(0, size));
        }
        else {
            result = new ArrayList<>(result.subList(0, 10));
        }

        return result;
    }

    public Post findPostById(long id) {
        return posts.get(id);
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
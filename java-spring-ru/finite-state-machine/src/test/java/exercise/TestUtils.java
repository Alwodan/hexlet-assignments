package exercise;

import exercise.model.Post;
import exercise.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    @Autowired
    private PostRepository repository;

    public long getPostId(String title) {
        Post post = repository.findByTitle(title);
        return (long) post.getId();
    }
}

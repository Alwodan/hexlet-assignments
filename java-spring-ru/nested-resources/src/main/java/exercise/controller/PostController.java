package exercise.controller;

import exercise.dto.PostDto;
import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;


@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping(path = "")
    public Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Post getPost(@PathVariable long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post" + id + "not found"));
    }

    @PostMapping(path = "")
    public Post createPost(@RequestBody PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.title());
        post.setBody(postDto.body());
        return postRepository.save(post);
    }

    @PatchMapping(path = "/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setTitle(postDto.title());
        post.setBody(postDto.body());

        return postRepository.save(post);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePost(@PathVariable long id) {

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postRepository.delete(post);
    }
}

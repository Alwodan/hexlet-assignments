package exercise.controller;

import exercise.dto.CommentDto;
import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;


@RestController
@RequestMapping("/posts/{postId}/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    // BEGIN
    @GetMapping("")
    public Iterable<Comment> getAllComments(@PathVariable long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @GetMapping("/{commentId}")
    public Comment getCommentById(@PathVariable long postId, @PathVariable long commentId) {
        return commentRepository
                .findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Idk what you want"));
    }

    @PostMapping("")
    public void createComment(@PathVariable long postId, @RequestBody CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setPost(postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("check your post")));

        commentRepository.save(comment);
    }

    @PatchMapping("/{commentId}")
    public void patchComment(@PathVariable long postId, @PathVariable long commentId, @RequestBody CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setPost(postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("check your post id")));
        if (commentRepository.findById(commentId).isEmpty()) {
            throw new ResourceNotFoundException("check your comment id");
        }
        comment.setId(commentId);

        commentRepository.save(comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        if (commentRepository.findByIdAndPostId(commentId, postId).isEmpty()) {
            throw new ResourceNotFoundException("where is your comment buddy");
        }
        commentRepository.deleteById(commentId);
    }
    // END
}

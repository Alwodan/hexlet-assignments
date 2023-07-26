package exercise;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final QueueListener listener;

    MessageController(QueueListener listener) {
        this.listener = listener;
    }

    @GetMapping(path = "")
    public List<String> getAllMessages() {
        return listener.getAllMessages();
    }
}

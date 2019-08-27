package techcourse.fakebook.web.controller.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/file")
public class MultiPartController {
    private static final Logger log = LoggerFactory.getLogger(MultiPartController.class);

    @GetMapping("/article/{fileName}")
    public ResponseEntity<byte[]> displayFile(@PathVariable String fileName) throws IOException {
        return ResponseEntity.ok().body(Files.readAllBytes(Paths.get("file/article/" + fileName)));
    }

    @GetMapping("/user/profile/{fileName}")
    public ResponseEntity<byte[]> showProfileImage(@PathVariable String fileName) throws IOException {
        return ResponseEntity.ok().body(Files.readAllBytes(Paths.get("file/user/profile/" + fileName)));
    }
}

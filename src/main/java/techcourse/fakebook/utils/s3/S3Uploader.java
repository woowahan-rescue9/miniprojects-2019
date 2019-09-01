package techcourse.fakebook.utils.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.exception.FileSaveException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
public class S3Uploader {
    private static final Logger log = LoggerFactory.getLogger(S3Uploader.class);

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Uploader(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile multipartFile, String dirName, String fileName) {
        try {
            File uploadFile = convert(multipartFile, fileName)
                    .orElseThrow(FileSaveException::new);

            return upload(uploadFile, dirName, fileName);
        } catch (IOException e) {
            log.error("FileSaveError : file write 실패");
            throw new FileSaveException();
        }
    }

    private String upload(File uploadFile, String dirName, String fileName) {
        String savePath = dirName + fileName;
        String uploadImageUrl = putS3(uploadFile, savePath);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file, String fileName) throws IOException {
        File convertFile = new File(fileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        log.error("FileSaveError : convert 실패");
        return Optional.empty();
    }
}

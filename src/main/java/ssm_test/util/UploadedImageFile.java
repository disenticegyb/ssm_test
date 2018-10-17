package ssm_test.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * 增加：上传文件类
 * 接收上传文件的注入
 * @author disentice
 *
 */
public class UploadedImageFile {

	MultipartFile image;
	  
    public MultipartFile getImage() {
        return image;
    }
  
    public void setImage(MultipartFile image) {
        this.image = image;
    }
  
}

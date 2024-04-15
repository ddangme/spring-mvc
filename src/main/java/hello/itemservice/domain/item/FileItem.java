package hello.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data
public class FileItem {

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}

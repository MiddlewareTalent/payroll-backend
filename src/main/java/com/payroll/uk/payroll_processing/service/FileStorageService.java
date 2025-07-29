package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.exception.DataValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileStorageService {

    private static final List<String> allowedTypes = List.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final long MAX_SIZE = 25 * 1024 * 1024; // 5 MB

    private static final String UPLOAD_DIR = "C:/Users/Public/Documents/";



    public Map<String,String> storeEmployeeDocuments( MultipartFile p45File, MultipartFile checklistFile) throws IOException {


        Map<String,String> fileData=new ConcurrentHashMap<>();
        if (p45File != null && !p45File.isEmpty()) {
            validateFile(p45File);
            String filePath = saveFile(p45File, "P45");
            fileData.put("P45",filePath);

        }

        if (checklistFile != null && !checklistFile.isEmpty()) {
            validateFile(checklistFile);
            String filePath = saveFile(checklistFile, "Checklist");
            fileData.put("Checklist",filePath);

        }
        return fileData;

    }

    private void validateFile(MultipartFile file) {
        if (!allowedTypes.contains(file.getContentType())) {
            throw new DataValidationException("Unsupported file type: " + file.getContentType());
        }
        if (file.getSize() > MAX_SIZE) {
            throw new DataValidationException("File too large. Max allowed is 5MB");
        }
    }

    private String saveFile(MultipartFile file, String fileType) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + fileType + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.createDirectories(filePath.getParent()); // Ensure directories exist
        Files.write(filePath, file.getBytes());
        return filePath.toString(); // stored in DB as path
    }

    public Map<String,String> storeLogoDocument( MultipartFile companyLogo) throws IOException {
        Map<String,String> fileData=new ConcurrentHashMap<>();
        if (companyLogo != null && !companyLogo.isEmpty()) {
            validateFile(companyLogo);
            String filePath = saveFile(companyLogo, "companyLogo");
            fileData.put("companyLogo",filePath);

        }
        return fileData;

    }
}




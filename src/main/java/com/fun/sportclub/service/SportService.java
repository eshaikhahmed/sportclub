package com.fun.sportclub.service;

import com.fun.sportclub.entity.SportEntity;
import com.fun.sportclub.repository.SportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class SportService {

    @Autowired
    private SportRepository sportRepository;

    @Value("${upload.customer.path}")
    private String uploadSportPath;

    public SportEntity saveSport(SportEntity sport){
        return sportRepository.save(sport);
    }

    public Optional<SportEntity> getById(Long Id){
        return sportRepository.findById(Id);
    }

    public List<SportEntity> findAll() {
        return sportRepository.findAll();
    }

    public String saveSportImages(SportEntity sportRequest, MultipartFile multipartFile, MultipartFile multipartFile2) throws IOException {
        String UPLOADED_FOLDER = uploadSportPath;
        String response = "Processing";
        MultipartFile image1 = multipartFile;
        MultipartFile image2 = multipartFile2;
        if(image1 != null) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String fileName = Objects.requireNonNull(image1.getOriginalFilename()).replace(" ", "");
            fileName = dtf.format(now)+"_"+fileName;

            //Save customer image1 file on upload location
            byte[] bytes = image1.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, bytes);

            //Save customer image1 file name
            sportRequest.setSportImage1(fileName);

            log.info("Saved first image "+fileName);
            response = "Done";
        }

        if(image2 != null) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String fileName = Objects.requireNonNull(image2.getOriginalFilename()).replace(" ", "");

            if(!fileName.isEmpty()) {
                fileName = dtf.format(now) + "_" + fileName;

                //Save customer image2 file on upload location
                byte[] bytes = image2.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + fileName);
                Files.write(path, bytes);

                //Save customer image2 file name
                sportRequest.setSportImage2(fileName);

                log.info("Saved second image " + fileName);
                response = "Done";
            }
        }

        return response;
    }
}

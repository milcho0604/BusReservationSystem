package com.example.bussystem.bus.service;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.bus.dto.BusListDto;
import com.example.bussystem.bus.dto.BusSaveDto;
import com.example.bussystem.bus.repository.BusRepository;
import com.example.bussystem.category.domain.Category;
import com.example.bussystem.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Transactional
public class BusService {

    private final BusRepository busRepository;
    private final CategoryRepository categoryRepository;
    private final S3Client s3Client;

    @Autowired
    public BusService(BusRepository busRepository, CategoryRepository categoryRepository, S3Client s3Client) {
        this.busRepository = busRepository;
        this.categoryRepository = categoryRepository;
        this.s3Client = s3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public Bus busCreate(BusSaveDto dto) {
        MultipartFile image = dto.getBusImage();
        Bus bus = null;
        Category category = categoryRepository.findByCategoryName(dto.getCategoryName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));
        try {
            bus = busRepository.save(dto.toEntity(category));
            byte[] bytes = image.getBytes();
            Path path = Paths.get("/Users/milcho/etc/tmp/",
                    bus.getId() + "_" + image.getOriginalFilename());
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            bus.updateImagePath(path.toString());

//            if (dto.getCategoryName().contains("직행")){
//                stockInventoryService.increaseStock(Bus.getId(), dto.getStock_quantity());
//            }
            // 위는 dirtyChecking 과정을 거쳐 변경을 감지한다. -> 다시 save 할 필요가 없음. !!
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.");
        }
        return bus;
    }


    @Transactional
    public Page<BusListDto> busList(Pageable pageable) {
        Page<Bus> buses = busRepository.findAll(pageable);
        return buses.map(a -> a.listFromEntity());
    }

    @Transactional
    public Bus busAwsCreate(BusSaveDto dto) {
        MultipartFile image = dto.getBusImage();
        Bus bus = null;
        Category category = categoryRepository.findByCategoryName(dto.getCategoryName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));
        try {
            bus = busRepository.save(dto.toEntity(category));
            byte[] bytes = image.getBytes();
            String filName = bus.getId() + "_" + image.getOriginalFilename();
            Path path = Paths.get("/Users/milcho/etc/tmp/", filName);
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filName)
                    .build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromFile(path)
            );
            String s3Path = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(filName)).toExternalForm();
            // 위는 dirtyChecking 과정을 거쳐 변경을 감지한다. -> 다시 save 할 필요가 없음. !!
            bus.updateImagePath(s3Path);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.");
        }
        return bus;
    }

}

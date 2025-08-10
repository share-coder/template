package com.jc.template.feature.usermgmt.service;//package com.jc.envelop.usermgmt.service;

import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.common.entity.User;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.jc.template.common.constant.UserApiMessage.USER_DOWNLOAD_ERROR;
import static com.jc.template.common.util.Utils.getPageable;


@Service
@Transactional
@Slf4j
public class UserDownloadService {

    private final UserRepository userRepository;

    public UserDownloadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public org.springframework.data.util.Pair<String, byte[]> findAllUsersForDownload(Integer pageNo, Integer pageSize, String sortBy,
                                                                                      String orderBy) throws IOException {
        PageRequest pageable = getPageable(pageNo, pageSize, sortBy, orderBy);
        Page<User> page = userRepository.findAll(pageable);

        List<UserDto> userDTOS = page.stream().map(UserDto::from).collect(Collectors.toList());
        String reportDownloadPath = "All_Users.csv";
        log.info("DownloadPath: {}", reportDownloadPath);
        FileWriter csvWriter = new FileWriter(reportDownloadPath);
        try {
            csvWriter.append("Id");
            csvWriter.append(",");
            csvWriter.append("First Name");
            csvWriter.append(",");
            csvWriter.append("Last Name");
            csvWriter.append(",");
            csvWriter.append("Email");
            csvWriter.append(",");
            csvWriter.append("User Id");
            csvWriter.append(",");
            csvWriter.append("Status");
            csvWriter.append(",");
            csvWriter.append("Address1");
            csvWriter.append(",");
            csvWriter.append("Address2");
            csvWriter.append(",");
            csvWriter.append("City");
            csvWriter.append(",");
            csvWriter.append("State");
            csvWriter.append(",");
            csvWriter.append("Country");
            csvWriter.append(",");
            csvWriter.append("Pin Code");
            csvWriter.append("\n");
            for (UserDto userDto : userDTOS) {
                csvWriter.append(String.valueOf(userDto.getId())).append(",");
                csvWriter.append(String.valueOf(userDto.getName())).append(",");
                csvWriter.append(String.valueOf(userDto.getEmail())).append(",");
                csvWriter.append(String.valueOf(userDto.getUserId())).append(",");
                csvWriter.append(String.valueOf(userDto.getStatus())).append(",");
                csvWriter.append(String.valueOf(userDto.getAddress())).append(",");
                csvWriter.append(String.valueOf(userDto.getCreatedAt()));
                csvWriter.append("\n");
            }
        } catch (Exception e) {
            log.error("## Failed to download users. Error Message:{}, Error trace: ", e.getMessage(), e);
            throw InvalidInputException.builder().code(USER_DOWNLOAD_ERROR.getCode())
                    .errorMessage(USER_DOWNLOAD_ERROR.getMessage()).build();
        } finally {
            csvWriter.flush();
            csvWriter.close();
        }
        byte[] byteArray = FileUtils.readFileToByteArray(new File(reportDownloadPath));
        FileUtils.deleteQuietly(new File(reportDownloadPath));
        return org.springframework.data.util.Pair.of("All_Partners.csv", byteArray);
    }
}

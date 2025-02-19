package com.server.delivery.domain.user.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.user.dto.request.CustomerUpdateRequestDto;
import com.server.delivery.domain.user.dto.response.UserResponseDto;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.repository.UserRepository;
import com.server.delivery.util.helper.UserHelper;
import com.server.delivery.util.s3image.S3ImageUtilImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final S3ImageUtilImpl s3ImageUtilImpl;

    @Override
    @Transactional
    public void updateUser(CustomerUpdateRequestDto customerUpdateRequestDto, MultipartFile profileImage, CustomUserDetail customUserDetail) {
        //유저 조회
        User user = userHelper.getUser(customUserDetail.getUsername());
        //현재 사진 삭제 및 업데이트
        if (!user.getProfileImage().isEmpty()) {
            s3ImageUtilImpl.deleteImageFromS3(user.getProfileImage());
        }
        String updatedIamge = s3ImageUtilImpl.uploadImageToS3(profileImage);
        log.trace("user ProfileImage successfully changed ");

        // 변경된 값만 업데이트
        user.setNickname(customerUpdateRequestDto.getNickname());
        user.setIsPublic(customerUpdateRequestDto.isPublic());
        user.setPhoneNumber(customerUpdateRequestDto.getPhoneNumber());
        user.setProfileImage(updatedIamge);
        log.trace("user info successfully changed ");
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long userId, CustomUserDetail customUserDetail) {
        //유저 검증
        userHelper.validateUser(customUserDetail.getUsername());
        //검색할 유저 조회
        User user = userHelper.getUser(customUserDetail.getUsername());

        return UserResponseDto.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageCustom<UserResponseDto> searchUser(CustomUserDetail customUserDetail, String keyword, Pageable pageable) {
        userHelper.getUser(customUserDetail.getUsername());

        // 기본 정렬 조건: 생성일 내림차순 → 수정일 내림차순
        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

        // pageable 객체에 기본 정렬 적용
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

        // Repository 검색 실행
        Page<User> userPage = userRepository.findByNicknameContaining(keyword, sortedPageable);

        // User 엔티티 → UserResponseDto 변환
        List<UserResponseDto> userDtoList = userPage.getContent().stream()
                .map(UserResponseDto::from) // UserResponseDto 변환 메서드 필요
                .toList();
        log.info("keyword ={}", keyword);
        log.info("userDtoList ={}", userDtoList);

        return new PageCustom<>(userDtoList, sortedPageable, userPage.getTotalElements());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(CustomUserDetail customUserDetail) {
        User user = userHelper.getUser(customUserDetail.getUsername());
        user.softDelete();
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}

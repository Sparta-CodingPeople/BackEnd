package com.server.delivery.domain.auth.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.common.jwt.JwtHelper;
import com.server.delivery.domain.auth.dto.request.CustomerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.OwnerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.SignInRequestDto;
import com.server.delivery.domain.master.dto.request.MasterSignInRequestDto;
import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.owner.repository.OwnerRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.repository.UserJpaRepository;
import com.server.delivery.util.helper.UserHelper;
import com.server.delivery.util.s3image.S3ImageUtilImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final S3ImageUtilImpl s3ImageUtilImpl;
    private final UserJpaRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final UserHelper userHelper;
    private final JwtHelper jwtHelper;

    @Value("${jwt.secret.key}")
    private String secretKey;

    // JWT 토큰 생성 정적 메서드
    private static String generateToken(User user, String secretKey) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("role", user.getUserRole())
                .claim("tokenIssuedAt", LocalDateTime.now().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 만료
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    private static SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Transactional
    public void createCustomerAccount(CustomerCreateRequestDto signUpRequestDto, MultipartFile profileImage) {
        //유저 중복가입 검사
        userHelper.validateUser(signUpRequestDto.getUsername());
        //비밀번호 암호화
        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        //이미지 저장
        String uploadedImage = s3ImageUtilImpl.uploadImageToS3(profileImage);

        //dto -> User
        User user = CustomerCreateRequestDto.from(signUpRequestDto, uploadedImage);

        userRepository.save(user);
    }

    @Transactional
    public void createOwnerAccount(OwnerCreateRequestDto requestDto, MultipartFile profileImage) {
        userHelper.validateUser(requestDto.getUsername());
        //비밀번호 암호화
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        //이미지 저장
        String uploadedImage = s3ImageUtilImpl.uploadImageToS3(profileImage);

        //dto -> User
        User user = OwnerCreateRequestDto.from(requestDto, uploadedImage);

        User savedUser = userRepository.save(user);
        Owner owner = Owner.builder().user(savedUser).businessNumber(requestDto.getBusinessNumber()).build();

        ownerRepository.save(owner);

    }

    @Transactional
    public String signInCustomer(SignInRequestDto signInRequestDto) {
        log.info("username = {} ", signInRequestDto.getUsername());
        User user = userHelper.getUser(signInRequestDto.getUsername());

        if (passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            user.updateTokenIssuedAt();
            return generateToken(user, secretKey);
        } else {
            throw new CustomUserException(ExceptionCode.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public String signInMaster(MasterSignInRequestDto masterSignInRequestDto) {

        User user = userHelper.getUser(masterSignInRequestDto.getUsername());

        //TODO::masterCode 해싱처리하고 비교하기
        if (passwordEncoder.matches(masterSignInRequestDto.getPassword(), user.getPassword())) {
            user.updateTokenIssuedAt();
            return generateToken(user, secretKey);
        } else if (!user.getMaster().getMasterCode().equals(masterSignInRequestDto.getMasterCode())) {
            throw new CustomUserException(ExceptionCode.BAD_REQUEST);
        } else {
            throw new CustomUserException(ExceptionCode.BAD_REQUEST);
        }
    }

    @Transactional
    public String renewToken(String bearertoken) {
        String accessToken = jwtHelper.resolveToken(bearertoken);
        User user = jwtHelper.getUserFromToken(accessToken);

        String renewToken = generateToken(user, secretKey);
        user.updateTokenIssuedAt();

        return renewToken;
    }
}
package com.example.villagerservice.member.api;

import com.example.document.member.CreateMemberDocumentationTemplate;
import com.example.document.BaseDocumentation;
import com.example.villagerservice.TestConfig;
import com.example.villagerservice.member.domain.Gender;
import com.example.villagerservice.member.dto.CreateMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;


@Import({TestConfig.class})
class AuthApiControllerTest extends BaseDocumentation {

    @Autowired
    private ObjectMapper objectMapper;

    private CreateMemberDocumentationTemplate template = new CreateMemberDocumentationTemplate();

    @Test
    @DisplayName("닉네임을 전달하지 않을 경우 테스트")
    void createMemberApiNickNameBlankTest() throws Exception {
        // given
        String memberJson = createMember("    ", "test@gamil.com", "Shwi18a78!!",
                Gender.MAN, 2022, 12, 5);

        String errorKey = "validation.nickname";
        String errorDescription = "닉네임은 필수입력 값입니다.";
        // when & then
        baseGiven(memberJson,
                template.createRestDocumentation(
                        "회원가입",
                        "닉네임 유효성 검사 실패",
                        errorKey, errorDescription)
        )
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("errorCode", Matchers.equalTo(DATA_INVALID_ERROR.getErrorCode()))
                .body("errorMessage", Matchers.equalTo(DATA_INVALID_ERROR.getErrorMessage()))
                .body("validation.nickname", Matchers.equalTo("닉네임은 필수입력 값입니다."));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "notformat@@"})
    @DisplayName("이메일 형식이 아닐 경우 테스트")
    void createMemberApiEmailBlankTest(String email) throws Exception {
        // given
        String memberJson = createMember("nickname1", email, "Shwi18a78!!",
                Gender.MAN, 2022, 12, 5);
        String errorKey = "validation.email";
        String errorDescription = "유효하지 않은 이메일 형식입니다.";

        // when & then
        baseGiven(memberJson,
                template.createRestDocumentation(
                        "회원가입",
                        "이메일 유효성 검사 실패",
                        errorKey, errorDescription)
        )
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("errorCode", Matchers.equalTo(DATA_INVALID_ERROR.getErrorCode()))
                .body("errorMessage", Matchers.equalTo(DATA_INVALID_ERROR.getErrorMessage()))
                .body(errorKey, Matchers.equalTo(errorDescription));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "     ", "ajsidk2", "Test1!!", "TesT12!!Test12!!!"})
    @DisplayName("비밀번호 형식이 맞지 않을경우 테스트")
    void createMemberApiPasswordNotFormatTest(String password) throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", password,
                Gender.MAN, 2022, 12, 5);

        String errorKey = "validation.password";
        String errorDescription = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.";

        // when & then

        baseGiven(memberJson,
                template.createRestDocumentation(
                        "회원가입",
                        "비밀번호 유효성 검사 실패",
                        errorKey, errorDescription)
        )
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("errorCode", Matchers.equalTo(DATA_INVALID_ERROR.getErrorCode()))
                .body("errorMessage", Matchers.equalTo(DATA_INVALID_ERROR.getErrorMessage()))
                .body(errorKey, Matchers.equalTo(errorDescription));
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createMemberApiTest() throws Exception {
        // given
        String memberJson = createMember("nickname1", "test@gmail.com", "helloWorld1!!",
                Gender.MAN, 2022, 12, 5);

        // when & then
        baseGiven(memberJson,
                template.createRestDocumentation(
                        "회원가입",
                        "회원가입 성공"))
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @NotNull
    private String createMember(String nickname, String email, String pass, Gender gender,
                                int year, int month, int day) throws JsonProcessingException {
        CreateMember.Request memberCreate = CreateMember.Request.builder()
                .nickname(nickname)
                .email(email)
                .password(pass)
                .gender(gender)
                .year(year)
                .month(month)
                .day(day)
                .build();

        return objectMapper.writeValueAsString(memberCreate);
    }
}
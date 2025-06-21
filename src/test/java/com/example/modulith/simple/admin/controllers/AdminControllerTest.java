package com.example.modulith.simple.admin.controllers;

import com.example.modulith.simple.admin.services.AdminSessionService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureRestDocs
@Import(AdminControllerTest.MockedAdminBeans.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminSessionService sessionService;

    @TestConfiguration
    static class MockedAdminBeans {
        @Bean
        public AdminSessionService adminSessionService() {
            return Mockito.mock(AdminSessionService.class);
        }
    }

    @Test
    public void shouldRegisterAdmin() throws Exception {
        given(sessionService.isAdminNameTaken("testadmin")).willReturn(false);
        given(sessionService.findSessionByAdmin("testadmin")).willReturn(Optional.of("test-session-id"));


        this.mockMvc.perform(post("/api/admins/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "testadmin"))
                .andExpect(status().isOk())
                .andDo(document("admin-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        formParameters(
                                parameterWithName("name").description("The name of the admin to register.")
                        ),
                        responseFields(
                                fieldWithPath("sessionId").description("The session ID for the newly registered admin.")
                        )
                ));
    }

    @Test
    public void shouldGetMe() throws Exception {
        given(sessionService.findAdminBySession(anyString())).willReturn(Optional.of("testadmin"));

        this.mockMvc.perform(get("/api/admins/me")
                        .header("Authorization", "Bearer test-session-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":\"testadmin\"}"))
                .andDo(document("admin-me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("The admin's session token (Bearer token).")
                        ),
                        responseFields(
                                fieldWithPath("name").description("The name of the currently logged-in admin.")
                        )
                ));
    }
}
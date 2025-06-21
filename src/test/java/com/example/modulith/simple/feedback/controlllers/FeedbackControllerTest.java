package com.example.modulith.simple.feedback.controlllers;

import com.example.modulith.simple.core.dto.DashboardDTO;
import com.example.modulith.simple.core.dto.FeedbackRequest;
import com.example.modulith.simple.core.session.AdminSessionRepository;
import com.example.modulith.simple.feedback.controllers.FeedbackController;
import com.example.modulith.simple.feedback.services.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackController.class)
@AutoConfigureRestDocs
@Import(FeedbackControllerTest.MockedBeans.class)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @TestConfiguration
    static class MockedBeans {
        @Bean
        public FeedbackService feedbackService() {
            return Mockito.mock(FeedbackService.class);
        }

        @Bean
        public AdminSessionRepository adminSessionRepository() {
            return Mockito.mock(AdminSessionRepository.class);
        }
    }

    @Test
    public void shouldSendFeedback() throws Exception {
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setAdminName("testadmin");
        feedbackRequest.setMessage("Great service!");
        feedbackRequest.setRating(5);

        this.mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedbackRequest)))
                .andExpect(status().isOk())
                .andDo(document("feedback-send",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("adminName").description("The name of the admin to send feedback to."),
                                fieldWithPath("message").description("The feedback message."),
                                fieldWithPath("rating").description("The rating from 1 to 5.")
                        )
                ));
    }

    @Test
    public void shouldGetMyFeedbacks() throws Exception {
        given(adminSessionRepository.findAdminBySession("test-session-id")).willReturn(Optional.of("testadmin"));
        given(feedbackService.getDashboardData("testadmin"))
                .willReturn(new DashboardDTO(Collections.emptyList(), 0.0));

        this.mockMvc.perform(get("/api/feedback/mine")
                        .header("Authorization", "Bearer test-session-id"))
                .andExpect(status().isOk())
                .andDo(document("feedback-mine",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("The admin's session token (Bearer token).")
                        ),
                        responseFields(
                                fieldWithPath("feedbacks").description("A list of feedback objects for the admin."),
                                fieldWithPath("averageRating").description("The average rating for the admin.")
                        )
                ));
    }
}
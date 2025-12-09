package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Feedback;
import com.sipsync.sipsync.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired FeedbackRepository feedbackRepo;

    public void saveFeedback(Long userId, String name, String email, String message){
        Feedback feedback = new Feedback(userId, name, email, message);

            feedbackRepo.save(feedback);
    }
}

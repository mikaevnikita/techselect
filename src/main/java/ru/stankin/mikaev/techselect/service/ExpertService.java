package ru.stankin.mikaev.techselect.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stankin.mikaev.techselect.dto.RecommendationDto;
import ru.stankin.mikaev.techselect.dto.RecommendationTextDto;
import ru.stankin.mikaev.techselect.dto.RecommendationsHolder;
import ru.stankin.mikaev.techselect.dto.SurveyDto;
import ru.stankin.mikaev.techselect.repository.RecommendationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ExpertService.
 *
 * @author Nikita_Mikaev
 */
@Service
public class ExpertService {

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private RecommendationRepository recommendationRepository;

    public List<RecommendationTextDto> resolve(SurveyDto surveyDto) {
        RecommendationsHolder holder = new RecommendationsHolder();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("recommendations", holder);
        kieSession.insert(surveyDto);
        kieSession.fireAllRules();
        kieSession.dispose();
        return holder.getRecommendations().stream()
                .flatMap(recommendationDto ->
                        recommendationRepository.findById(recommendationDto.getRecommendationId()).stream())
                .map(entity -> new RecommendationTextDto(entity.getId(), entity.getText()))
                .collect(Collectors.toList());
    }
}

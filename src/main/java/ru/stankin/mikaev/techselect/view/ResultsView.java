package ru.stankin.mikaev.techselect.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stankin.mikaev.techselect.dto.QuestionDto;
import ru.stankin.mikaev.techselect.dto.RecommendationDto;
import ru.stankin.mikaev.techselect.dto.RecommendationTextDto;
import ru.stankin.mikaev.techselect.dto.SurveyDto;
import ru.stankin.mikaev.techselect.exception.SurveyNotCompletedException;
import ru.stankin.mikaev.techselect.service.ExpertService;
import ru.stankin.mikaev.techselect.service.SessionService;
import ru.stankin.mikaev.techselect.service.SurveyService;
import java.util.List;

/**
 * ResultsView.
 *
 * @author Nikita_Mikaev
 */
@Route("results")
@PageTitle("Результаты")
@Slf4j
public class ResultsView extends Div implements RouterLayout, AfterNavigationObserver {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ExpertService expertService;

    private final Accordion accordion = new Accordion();

    public ResultsView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        H1 welcome = new H1("Результаты");
        verticalLayout.add(welcome, accordion);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        add(verticalLayout);
    }

    public void showResults(SurveyDto surveyDto) {
        List<RecommendationTextDto> recommendations = expertService.resolve(surveyDto);

        for (int i =0; i < recommendations.size(); i++) {
            RecommendationTextDto currentRecommendation = recommendations.get(i);
            accordion.add("Рекомендация " + (i + 1),
                    new Text("Сработало бизнес правило : " + currentRecommendation.getText()));
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        try {
            SurveyDto completedSurvey = surveyService.getCompletedSurvey(sessionService.getSessionId());
            showResults(completedSurvey);
        } catch (SurveyNotCompletedException ex) {
            UI.getCurrent().navigate(MainView.class);
        }
    }
}

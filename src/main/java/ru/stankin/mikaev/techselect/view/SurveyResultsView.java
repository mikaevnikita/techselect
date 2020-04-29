package ru.stankin.mikaev.techselect.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stankin.mikaev.techselect.constants.MainViewStaticTexts;
import ru.stankin.mikaev.techselect.dto.AnswerDto;
import ru.stankin.mikaev.techselect.dto.QuestionDto;
import ru.stankin.mikaev.techselect.dto.SurveyDto;
import ru.stankin.mikaev.techselect.exception.SurveyNotCompletedException;
import ru.stankin.mikaev.techselect.service.SessionService;
import ru.stankin.mikaev.techselect.service.SurveyService;

/**
 * SurveyResultsView.
 *
 * @author Nikita_Mikaev
 */
@Route("results")
@PageTitle("Результаты")
@Slf4j
public class SurveyResultsView extends Div implements RouterLayout, AfterNavigationObserver {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SessionService sessionService;

    private final Accordion accordion = new Accordion();

    public SurveyResultsView() {
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

    public void showSurvey(SurveyDto surveyDto) {
        for (QuestionDto questionDto : surveyDto.getQuestions()) {
            VerticalLayout answersLayout = new VerticalLayout();
            for (int i = 0; i < questionDto.getAnswers().size(); i ++) {
                String currentAnswerText = questionDto.getAnswers().get(i).getText();
                answersLayout.add(new Details("Ответ " + (i + 1),
                        new Text(currentAnswerText)));
            }
            accordion.add(questionDto.getText(), answersLayout);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        try {
            SurveyDto completedSurvey = surveyService.getCompletedSurvey(sessionService.getSessionId());
            showSurvey(completedSurvey);
        } catch (SurveyNotCompletedException ex) {
            UI.getCurrent().navigate("main");
        }
    }
}

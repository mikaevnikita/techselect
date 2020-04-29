package ru.stankin.mikaev.techselect.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stankin.mikaev.techselect.dto.AnswerDto;
import ru.stankin.mikaev.techselect.dto.QuestionDto;
import ru.stankin.mikaev.techselect.service.SessionService;
import ru.stankin.mikaev.techselect.service.SurveyService;
import java.util.Optional;

/**
 * SurveyView.
 *
 * @author Nikita_Mikaev
 */
@Route("survey")
@PageTitle("Анкета")
@Slf4j
public class SurveyView extends Div implements RouterLayout, AfterNavigationObserver {

    private final VerticalLayout verticalLayout;

    private RadioButtonGroup<AnswerDto> answerGroup;

    private final H1 h1 = new H1();

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SessionService sessionService;

    private QuestionDto currentQuestion;

    private RadioButtonGroup<AnswerDto> buildAnswerGroup() {
        RadioButtonGroup<AnswerDto> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setRenderer(new TextRenderer<>(AnswerDto::getText));
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        return radioButtonGroup;
    }

    private VerticalLayout buildVerticalLayout() {
        VerticalLayout newLayout = new VerticalLayout();

        newLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        newLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        newLayout.setPadding(false);
        newLayout.setMargin(true);
        newLayout.setSpacing(true);

        return newLayout;
    }

    public SurveyView() {
        verticalLayout = buildVerticalLayout();

        answerGroup = buildAnswerGroup();

        Button prevQuestionButton = new Button("Назад", new Icon(VaadinIcon.ARROW_LEFT));
        prevQuestionButton.addClickListener(event -> showPrevQuestion());
        Button nextQuestionButton = new Button("Далее", new Icon(VaadinIcon.ARROW_RIGHT));
        nextQuestionButton.addClickListener(event -> submitAnswer());
        nextQuestionButton.setIconAfterText(true);
        HorizontalLayout buttonsLayout = new HorizontalLayout(prevQuestionButton, nextQuestionButton);

        verticalLayout.add(h1, new Div(), answerGroup, buttonsLayout);

        add(verticalLayout);
    }

    public void setH1Text(Long questionId) {
        h1.setText("Вопрос " + questionId);
    }

    public void render(QuestionDto questionDto) {
        currentQuestion = questionDto;

        answerGroup.setItems(currentQuestion.getAnswers());
        currentQuestion.getSelectedAnswer().ifPresent(answerGroup::setValue);
        answerGroup.setLabel(currentQuestion.getText());
        setH1Text(currentQuestion.getId());
    }

    public void showPrevQuestion() {
        Optional<QuestionDto> prevQuestion = surveyService.getPrevQuestion(currentQuestion, sessionService.getSessionId());
        if (prevQuestion.isPresent()) {
            QuestionDto questionDto = prevQuestion.get();
            render(questionDto);
        }
    }

    public void submitAnswer() {
        if (answerGroup.getValue() != null) {
            surveyService.sumbitAnswer(currentQuestion, answerGroup.getValue(), sessionService.getSessionId());
            Optional<QuestionDto> nextQuestion =
                    surveyService.getNextQuestion(currentQuestion, sessionService.getSessionId());
            nextQuestion.ifPresentOrElse(this::render,
                    () -> UI.getCurrent().navigate(ResultsView.class));
        } else {
            Notification.show("Необходимо выбрать хотя бы один вариант ответа.");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        surveyService.getNextQuestion(currentQuestion, sessionService.getSessionId()).ifPresentOrElse(this::render,
                () -> UI.getCurrent().navigate(ResultsView.class));
    }
}

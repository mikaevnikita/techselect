package ru.stankin.mikaev.techselect.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
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
import ru.stankin.mikaev.techselect.view.component.MarkdownTextArea;
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

    private final IntegerField lifetimeTextField = new IntegerField();

    private final IntegerField userGrowthTextField = new IntegerField();

    public ResultsView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        H1 welcome = new H1("Результаты");
        verticalLayout.add(welcome, accordion);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        Button startNewSessionButton = new Button("Начать заново");
        startNewSessionButton.addClickListener(e -> startNewSession());
        verticalLayout.add(startNewSessionButton);
        add(verticalLayout);
    }

    public void startNewSession() {
        sessionService.startNewSession();
        UI.getCurrent().navigate(SurveyView.class);
    }

    public String template(String text) {
        return String.format("<marked-element>" +
                "<script type=\"text/markdown\">" +
                "%s" +
                "</script>" +
                "</marked-element>", text);
    }

    public void showResults(SurveyDto surveyDto) {
        List<RecommendationTextDto> recommendations = expertService.resolve(surveyDto);

        for (int i =0; i < recommendations.size(); i++) {
            RecommendationTextDto currentRecommendation = recommendations.get(i);
            if (currentRecommendation.getId() == 15L) {
                //показываем форму расчета нагрузки
                VerticalLayout potentialLoadLayout = new VerticalLayout();

                Button potentialLoadCalculateButton = new Button("Рассчитать");
                potentialLoadCalculateButton.addClickListener(e -> calculatePotentialLoad());

                potentialLoadLayout.add(new Text("Укажите срок эксплуатации системы в месяцах :"));
                potentialLoadLayout.add(lifetimeTextField);
                potentialLoadLayout.add(new Text("Укажите ожидаемый прирост пользователей в месяц :"));
                potentialLoadLayout.add(userGrowthTextField);
                potentialLoadLayout.add(potentialLoadCalculateButton);

                accordion.add("Расчет потенциальной нагрузки системы",
                        potentialLoadLayout);
            } else {
                accordion.add("Рекомендация " + (i + 1),
                        new Html(template(currentRecommendation.getText())));
            }
        }
    }

    public void calculatePotentialLoad() {
        Integer lifetimeMonts = lifetimeTextField.getValue();
        Integer userGrowthPerMonth = userGrowthTextField.getValue();
        Long potentialLoad = (long) (lifetimeMonts * userGrowthPerMonth);
        Dialog dialog = new Dialog();

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout vl = new VerticalLayout();
        Html messageLabel = new Html(template(String.format("Потенциальная нагрузка на сервис составит : **%d пользователей**.  \n" +
                "Расчитывайте свои производственные мощности ориентируясь на данное число.", potentialLoad)));
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        vl.add(messageLabel, cancelButton);
        dialog.add(vl);
        dialog.open();
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

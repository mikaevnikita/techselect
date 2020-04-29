package ru.stankin.mikaev.techselect.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import ru.stankin.mikaev.techselect.constants.MainViewStaticTexts;

/**
 * MainView.
 *
 * @author Nikita_Mikaev
 */
@Route
@PageTitle("TechSelect")
public class MainView extends Div implements RouterLayout {

    public MainView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        H1 welcome = new H1(MainViewStaticTexts.welcome);
        Label about = new Label(MainViewStaticTexts.about);
        Button startSurveyButton = new Button(MainViewStaticTexts.startSurveyButtonText);
        startSurveyButton.addClickListener(e -> {
            UI.getCurrent().navigate(SurveyView.class);
        });
        Div div = new Div();
        verticalLayout.setSizeFull();
        verticalLayout.add(welcome, div, about, startSurveyButton);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        add(verticalLayout);
    }
}

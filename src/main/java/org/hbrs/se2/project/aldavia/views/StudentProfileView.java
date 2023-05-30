package org.hbrs.se2.project.aldavia.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.aldavia.control.StudentProfileControl;
import org.hbrs.se2.project.aldavia.control.exception.ProfileException;
import org.hbrs.se2.project.aldavia.dtos.StudentProfileDTO;
import org.hbrs.se2.project.aldavia.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Route(value = Globals.Pages.PROFILE_VIEW, layout = LoggedInStateLayout.class)
@PageTitle("Profil")
public class StudentProfileView extends Div implements HasUrlParameter<String> {

    @Autowired
    private StudentProfileControl studentProfileControl;

    private StudentProfileDTO studentProfileDTO;
    private H1 name = new H1();
    private H2 title = new H2();
    private Text description = new Text("");
    private Div profilePicture = new Div();
    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        List<String> usernames = parametersMap.get("username");
        if(usernames != null && !usernames.isEmpty()){
            try {
                studentProfileDTO = studentProfileControl.getStudentProfile(usernames.get(0));
                Notification.show("Benutzername: " + studentProfileDTO.getVorname() + " " + studentProfileDTO.getNachname());
                add(createIntroductionLayout());
            } catch (ProfileException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Handle the case where there is no "username" parameter or the list is empty
            Notification.show("Kein Benutzername angegeben");
            // TODO: Redirect to 404 Page
        }
    }
    public StudentProfileView(StudentProfileControl studentProfileControl) {
        addClassName("profile-view");
    }


    private HorizontalLayout createIntroductionLayout(){
        HorizontalLayout introductionLayout = new HorizontalLayout();
        introductionLayout.addClassName("introduction");
        introductionLayout.add(profilePicture);
        introductionLayout.add(createDescriptionLayout());
        return introductionLayout;
    }

    private VerticalLayout createDescriptionLayout(){
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.addClassName("description");
        name = new H1(studentProfileDTO.getVorname() + " " + studentProfileDTO.getNachname());
        title = new H2(studentProfileDTO.getStudiengang());
        description = new Text(studentProfileDTO.getBeschreibung());
        descriptionLayout.add(name);
        descriptionLayout.add(title);
        descriptionLayout.add(description);
        return descriptionLayout;
    }


    // TEST TEST

}
